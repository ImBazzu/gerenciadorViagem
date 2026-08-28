package br.com.imbazzu.secretariaTransporte.segurança;

import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RetornoQueryConfig extends OncePerRequestFilter {

    private final EntityManager entityManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean eAdm = usuarioEAdm();

        if (!eAdm) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("deletadoFilter").setParameter("deletadoParam", false);
        }
        // se for admin, simplesmente não habilita o filtro -> vê tudo (ativo true e false)

        filterChain.doFilter(request, response);

    }

    private boolean usuarioEAdm(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
