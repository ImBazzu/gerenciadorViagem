package br.com.imbazzu.secretariaTransporte.segurança;

import br.com.imbazzu.secretariaTransporte.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenServiceJwt {

    @Value("${api.security.token.secret}")
    private String secrets;

    @Value("${api.security.token.assinatura}")
    private String assinatura;

    /**
     * Buscar o "token" no cabeçalho da requisição
     * 
     * @param request RequisiçãoHttp
     * @return token JWT
     */
    public String recuperarToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        String token = authorization.substring(7);

        if (token.isBlank()) {
            return null;
        }

        return token;
    }

    public String gerarToken(Usuario usuario) {
        var roles = usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Algorithm algorithm = Algorithm.HMAC256(secrets);

        return JWT.create()
                .withIssuer(assinatura)
                .withSubject(usuario.getNome())
                .withClaim("roles", roles)
                .withClaim("type", "access")
                .withExpiresAt(
                        Instant.now().plus(15, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public String gerarAttToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secrets);
        return JWT.create()
                .withIssuer(assinatura)
                .withSubject(usuario.getNome())
                .withClaim("type", "refresh")
                .withExpiresAt(Instant.now().plus(8, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public DecodedJWT validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secrets);

        return JWT.require(algorithm)
                .withIssuer(assinatura)
                .withClaim("type", "access")
                .build()
                .verify(token);

    }

}
