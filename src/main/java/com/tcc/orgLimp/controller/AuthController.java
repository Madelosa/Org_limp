package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.LoginRequest;
import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

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
                        HttpServletRequest request,
                        HttpServletResponse response,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        System.out.println("[AUTH CONTROLLER] Tentativa de login com email: " + loginRequest.getEmail());
        Usuario usuario = authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "E-mail ou senha inválidos.");
            return "redirect:/login";
        }

        session.setAttribute("usuario", usuario);

        // Criar Authentication para o Spring Security
        String role = "ROLE_" + usuario.getPerfil().name().toUpperCase();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );

        // Criar novo SecurityContext e definir a authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // Salvar EXPLICITAMENTE na session HTTP para persistir entre requisições
        SecurityContextRepository repository = new HttpSessionSecurityContextRepository();
        repository.saveContext(context, request, response);

        System.out.println("[AUTH CONTROLLER] Login realizado: " + usuario.getEmail() + " | Role: " + role);

        if (usuario.getPerfil() == Usuario.Perfil.gerente) {
            return "redirect:/gerente/tarefas";
        } else if (usuario.getPerfil() == Usuario.Perfil.supervisor) {
            return "redirect:/supervisor/tarefas";
        }

        session.invalidate();
        redirectAttributes.addFlashAttribute("error", "Perfil não autorizado.");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        session.invalidate();
        return "redirect:/login?logout";
    }
}
