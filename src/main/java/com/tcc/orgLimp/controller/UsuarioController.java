
package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.UsuarioRequest;
import com.tcc.orgLimp.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/usuarios/salvar")
    public String salvar(
            @Valid @ModelAttribute UsuarioRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.salvar(request);
            redirectAttributes.addFlashAttribute(
                    "success", "Usuário salvo com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Erro ao salvar usuário: " + e.getMessage());
        }

        return "redirect:/gerente/usuarios";
    }

    @PostMapping("/usuarios/{id}/deletar")
    public String deletar(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.deletarUsuario(id, authentication.getName());
            redirectAttributes.addFlashAttribute(
                    "success", "Usuário deletado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Erro ao deletar usuário: " + e.getMessage());
        }

        return "redirect:/gerente/usuarios";
    }

    @PostMapping("/gerente/perfil/excluir")
    public String excluirPropriaConta(
            Authentication authentication,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.excluirPropriaConta(authentication.getName());

            SecurityContextHolder.clearContext();
            session.invalidate();

            return "redirect:/login?contaExcluida";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Erro ao excluir sua conta: " + e.getMessage());

            return "redirect:/gerente/perfil";
        }
    }
}