package br.com.imbazzu.secretariaTransporte.segurança;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityFiltroToken extends OncePerRequestFilter {

    private final TokenServiceJwt tokenService;

    @Override
    protected @NullMarked void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // Recupera do "token" da requisição
        var token = tokenService.recuperarToken(request);

        // Verificar se o token é vazio

        if (token != null && !token.isBlank()) {
            try {
                // Valida o Token
                var decodedJWT = tokenService.validarToken(token);

                // Busca o usuario no Token
                var login = decodedJWT.getSubject();
                // Pega a permissão do usuario
                var roles = decodedJWT.getClaim("roles");

                List<? extends GrantedAuthority> autoridades;

                // Converte as permissões em autoridades
                if (roles.isNull()) {
                    autoridades = List.of();
                } else {
                    autoridades = roles.asList(String.class)
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                }

                // Gera um "token" autenticado com o nome do usuario e as autoridades
                var authentication = new UsernamePasswordAuthenticationToken(login, null, autoridades);
                // Informa ao Spring Security que o usuario está autenticado
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch(Exception e){
                System.out.println("Erro validando JWT");
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
        // Chama o próximo Filtro
    }
}
