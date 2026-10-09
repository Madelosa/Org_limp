
package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.UsuarioSessao;
import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.service.NotificacaoService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @PostMapping("/notificacoes/{id}/ler")
    public String marcarComoLida(
            @PathVariable Long id,
            HttpSession session) {

        UsuarioSessao usuario =
                (UsuarioSessao) session.getAttribute("usuario");

        notificacaoService.marcarComoLida(id, usuario.getId());

        if (usuario.getPerfil() == Usuario.Perfil.gerente) {
            return "redirect:/gerente/notificacoes";
        }

        return "redirect:/supervisor/notificacoes";
    }

    @PostMapping("/notificacoes/ler-todas")
    public String marcarTodasComoLidas(HttpSession session) {

        UsuarioSessao usuario =
                (UsuarioSessao) session.getAttribute("usuario");

        notificacaoService.marcarTodasComoLidas(usuario.getId());

        if (usuario.getPerfil() == Usuario.Perfil.gerente) {
            return "redirect:/gerente/notificacoes";
        }

        return "redirect:/supervisor/notificacoes";
    }
}