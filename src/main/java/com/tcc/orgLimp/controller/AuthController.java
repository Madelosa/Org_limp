package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.LoginRequest;
import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/";
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "pages/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest loginRequest,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        Usuario usuario = authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "E-mail ou senha inválidos.");
            return "redirect:/login";
        }

        session.setAttribute("usuario", usuario);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
