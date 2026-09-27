package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.service.NotificacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/notificacoes/{id}/ler")
    public String marcarComoLida(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        notificacaoService.marcarComoLida(id);

        if (usuario.getPerfil() == Usuario.Perfil.gerente) {
            return "redirect:/gerente/notificacoes";
        }
        return "redirect:/supervisor/notificacoes";
    }

    @GetMapping("/notificacoes/ler-todas")
    public String marcarTodasComoLidas(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        notificacaoService.marcarTodasComoLidas(usuario.getId());

        if (usuario.getPerfil() == Usuario.Perfil.gerente) {
            return "redirect:/gerente/notificacoes";
        }
        return "redirect:/supervisor/notificacoes";
    }
}
