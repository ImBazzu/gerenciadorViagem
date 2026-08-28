// br.com.imbazzu.frontSpring.config.JwtAuthorizationFilter
package br.com.imbazzu.frontSpring.config;

import br.com.imbazzu.frontSpring.exception.AuthenticationExpiredException;
import br.com.imbazzu.frontSpring.security.SessionAuth;
import br.com.imbazzu.frontSpring.util.ServletUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;

import reactor.core.publisher.Mono;

import jakarta.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements ExchangeFilterFunction {

    private final SessionAuth tokenStore;

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        HttpSession session = ServletUtils.currentSession(false);
        String token = tokenStore.access(session);

        if (token == null || token.isEmpty()) {
            throw new AuthenticationExpiredException();
        }
        ClientRequest authorized = ClientRequest.from(request)
                .headers(h -> h.setBearerAuth(token))
                .build();
        return next.exchange(authorized);

    }
}
