// br.com.imbazzu.frontSpring.util.ServletUtils
package br.com.imbazzu.frontSpring.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class ServletUtils {

    private ServletUtils() {}

    public static HttpSession currentSession(boolean create) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra instanceof ServletRequestAttributes sra) {
            HttpServletRequest req = sra.getRequest();
            return req.getSession(create);
        }
        return null;
    }
}
