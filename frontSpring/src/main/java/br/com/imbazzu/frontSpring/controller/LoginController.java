// br.com.imbazzu.frontSpring.controller.LoginController
package br.com.imbazzu.frontSpring.controller;

import br.com.imbazzu.frontSpring.dto.login.LoginRequestDto;
import br.com.imbazzu.frontSpring.config.AuthService;
import br.com.imbazzu.frontSpring.security.SessionAuth;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;
    private final SessionAuth tokenStore;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        try {
            var response = authService.login(new LoginRequestDto(username, password));
            tokenStore.store(session, response.tokenAcesso(), response.refreshToken());
            session.setAttribute("usuario", username);
            return "redirect:/";
        } catch (Exception ex) {
            String mensagem = "Usuário ou senha incorreto";
            model.addAttribute("error", mensagem);
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        tokenStore.clear(session);
        return "redirect:/";
    }
}
