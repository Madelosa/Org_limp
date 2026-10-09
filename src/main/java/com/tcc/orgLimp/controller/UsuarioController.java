package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.UsuarioRequest;
import com.tcc.orgLimp.service.UsuarioService;
import jakarta.validation.Valid;
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
    public String salvar(@Valid @ModelAttribute UsuarioRequest request, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.salvar(request);
            redirectAttributes.addFlashAttribute("success", "Usuário salvo com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar usuário: " + e.getMessage());
        }
        return "redirect:/gerente/usuarios";
    }

    @PostMapping("/usuarios/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Usuário deletado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar usuário: " + e.getMessage());
        }
        return "redirect:/gerente/usuarios";
    }
}
