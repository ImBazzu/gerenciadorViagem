// br.com.imbazzu.frontSpring.security.SessionTokenStore
package br.com.imbazzu.frontSpring.security;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionAuth {

    private static final String ACCESS = "ACCESS_TOKEN";
    private static final String REFRESH = "REFRESH_TOKEN";
    private static final String LOCK = "TOKEN_LOCK";

    public void store(HttpSession session, String accessToken, String refreshToken) {
        session.setAttribute(ACCESS, accessToken);
        session.setAttribute(REFRESH, refreshToken);
        if (session.getAttribute(LOCK) == null) {
            session.setAttribute(LOCK, new Object());
        }
    }

    public String access(HttpSession session) {
        return session == null ? null : (String) session.getAttribute(ACCESS);
    }

    public String refresh(HttpSession session) {
        return session == null ? null : (String) session.getAttribute(REFRESH);
    }

    public Object lock(HttpSession session) {
        Object l = session == null ? null : session.getAttribute(LOCK);
        if (l == null && session != null) {
            l = new Object();
            session.setAttribute(LOCK, l);
        }
        return l;
    }

    public void clear(HttpSession session) {
        if (session != null) session.invalidate();
    }
}
