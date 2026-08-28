package br.com.imbazzu.frontSpring.controller;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.imbazzu.frontSpring.service.HomeService;

@Controller
@RequestMapping
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        homeService.verificarLogado(session);
        return "paginainicial/index";
    }

}
