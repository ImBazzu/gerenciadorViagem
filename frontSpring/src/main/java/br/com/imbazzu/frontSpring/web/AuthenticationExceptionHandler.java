// br.com.imbazzu.frontSpring.web.AuthenticationExceptionHandler
package br.com.imbazzu.frontSpring.web;

import br.com.imbazzu.frontSpring.exception.AuthenticationExpiredException;
import br.com.imbazzu.frontSpring.exception.FormularioError;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxReswap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(AuthenticationExpiredException.class)
    public Object handleAuthExpired(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();

        String hxRequest = request.getHeader("HX-Request");
        if (hxRequest != null) {
            // Requisição via HTMX -> instruir redirect via header HX-Redirect
            return ResponseEntity.ok()
                    .header("HX-Redirect", "/login")
                    .build();
        }

        // Requisição normal -> redirect servidor para /login
        return "redirect:/login";
    }

    @ExceptionHandler(FormularioError.class)
    public String handleFormularioError(FormularioError e, Model model, HtmxResponse response) {
        String mensagem = extrairMensagem(e.getMessage());
        model.addAttribute("mensagem", mensagem);
        response.setReswap(HtmxReswap.innerHtml());
        response.setRetarget("#error");
        return "fragments/formulario :: error";
    }

    private String extrairMensagem(String raw) {
        Matcher matcher = Pattern.compile("\"mensagem\":\"([^\"]+)\"").matcher(raw);
        return matcher.find() ? matcher.group(1) : "Erro ao salvar destino.";
    }

}
