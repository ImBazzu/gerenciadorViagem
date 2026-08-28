package br.com.imbazzu.secretariaTransporte.segurança;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.autenticacao.LoginBloqueadoException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoginTentativasService {

    private static final int LIMITE_TENTATIVAS = 5;

    private final Cache<String, AtomicInteger> tentativas = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    public void validarBloqueio(String chave) {
        var contador = tentativas.getIfPresent(chave);
        if (contador != null && contador.get() >= LIMITE_TENTATIVAS) {
            throw new LoginBloqueadoException(
                    "Muitas tentativas de login. Tente novamente em alguns minutos.");
        }
    }

    public void registrarFalha(String chave) {
        tentativas.asMap()
                .computeIfAbsent(chave, k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public void limparTentativas(String chave) {
        tentativas.invalidate(chave);
    }
}