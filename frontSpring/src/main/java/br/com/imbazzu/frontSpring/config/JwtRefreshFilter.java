// br.com.imbazzu.frontSpring.config.JwtRefreshFilter
package br.com.imbazzu.frontSpring.config;

import br.com.imbazzu.frontSpring.security.SessionAuth;
import br.com.imbazzu.frontSpring.exception.AuthenticationExpiredException;
import br.com.imbazzu.frontSpring.util.ServletUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import jakarta.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class JwtRefreshFilter implements ExchangeFilterFunction {

    private final SessionAuth tokenStore;
    private final AuthService authService;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {

        // executa a requisição original
        return next.exchange(request)
                .flatMap(response -> {
                    if (response.statusCode().value() != 401) {
                        return Mono.just(response);
                    }

                    // 401 -> tentar refresh (single-flight por sessão)
                    HttpSession session = ServletUtils.currentSession(false);
                    Object lock = tokenStore.lock(session);

                    if (lock == null) {
                        // sem sessão -> não é possível renovar
                        return Mono.error(new AuthenticationExpiredException());
                    }

                    // encapsula a operação de refresh em boundedElastic (evita bloquear thread reativa)
                    return Mono.fromCallable(() -> {
                                synchronized (lock) {
                                    String refreshToken = tokenStore.refresh(session);
                                    if (refreshToken == null) {
                                        tokenStore.clear(session);
                                        throw new AuthenticationExpiredException();
                                    }
                                    // chama o AuthService (blocking) mas estamos em boundedElastic
                                    var tokens = authService.refresh(refreshToken);
                                    tokenStore.store(session, tokens.tokenAcesso(), tokens.refreshToken());
                                    return tokens.tokenAcesso();
                                }
                            })
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(newAccessToken -> {
                                // re-executa a request original com o novo token
                                ClientRequest retry = ClientRequest.from(request)
                                        .headers(h -> h.setBearerAuth(newAccessToken))
                                        .build();
                                return next.exchange(retry);
                            })
                            .onErrorMap(ex -> {
                                if (ex instanceof AuthenticationExpiredException) return ex;
                                // se refresh falhou por motivo do authService (ex. 4xx), tratamos como sessão expirada
                                return new AuthenticationExpiredException();
                            });
                });
    }
}
