package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.LoginRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session,
                             @RequestParam(value = "error", required = false) String error) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/";
        }
        model.addAttribute("loginRequest", new LoginRequest());
        if (error != null) {
            model.addAttribute("error", "E-mail ou senha inválidos.");
        }
        return "pages/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
        return "redirect:/login?logout";
    }
}
