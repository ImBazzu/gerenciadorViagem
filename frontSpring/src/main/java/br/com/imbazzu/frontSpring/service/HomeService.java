package br.com.imbazzu.frontSpring.service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Service;

import br.com.imbazzu.frontSpring.exception.AuthenticationExpiredException;
import br.com.imbazzu.frontSpring.security.SessionAuth;

@Service
public class HomeService {

    private final SessionAuth sessionAuth;

    public HomeService(SessionAuth sessionAuth) {
        this.sessionAuth = sessionAuth;
    }

    public void verificarLogado(HttpSession session) {

        Optional.ofNullable(sessionAuth.access(session))
                .orElseThrow(() -> new AuthenticationExpiredException());
    }
}