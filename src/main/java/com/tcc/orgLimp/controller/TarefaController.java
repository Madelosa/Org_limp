package com.tcc.orgLimp.controller;

import org.springframework.validation.BindingResult;
import com.tcc.orgLimp.dto.UsuarioSessao;
import jakarta.servlet.http.HttpSession;
import com.tcc.orgLimp.dto.TarefaRequest;
import com.tcc.orgLimp.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping("/tarefas/salvar")
    public String salvar(
            @Valid @ModelAttribute TarefaRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mensagem = bindingResult.getFieldErrors()
                    .stream()
                    .map(erro -> erro.getDefaultMessage())
                    .filter(java.util.Objects::nonNull)
                    .findFirst()
                    .orElse("Verifique os dados informados.");

            redirectAttributes.addFlashAttribute("error", mensagem);
            return "redirect:/gerente/tarefas";
        }

        try {
            tarefaService.salvar(request);
            redirectAttributes.addFlashAttribute(
                    "success", "Tarefa salva com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Erro ao salvar tarefa: " + e.getMessage());
        }

        return "redirect:/gerente/tarefas";
    }

    @PostMapping("/tarefas/{id}/status")
    public String atualizarStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String observacao,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        UsuarioSessao usuario = (UsuarioSessao) session.getAttribute("usuario");

        try {
            tarefaService.atualizarStatus(
                    id,
                    status,
                    observacao,
                    usuario.getId());

            redirectAttributes.addFlashAttribute(
                    "success", "Status atualizado com sucesso.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Erro ao atualizar status: " + e.getMessage());
        }

        return "redirect:/supervisor/tarefas";
    }

    @PostMapping("/tarefas/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tarefaService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Tarefa deletada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar tarefa: " + e.getMessage());
        }
        return "redirect:/gerente/tarefas";
    }
}
