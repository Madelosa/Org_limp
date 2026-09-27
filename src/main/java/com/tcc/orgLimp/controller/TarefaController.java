package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.TarefaRequest;
import com.tcc.orgLimp.entity.Tarefa;
import com.tcc.orgLimp.service.TarefaService;
import jakarta.servlet.http.HttpSession;
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
    public String salvar(@Valid @ModelAttribute TarefaRequest request, RedirectAttributes redirectAttributes) {
        try {
            tarefaService.salvar(request);
            redirectAttributes.addFlashAttribute("success", "Tarefa salva com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar tarefa: " + e.getMessage());
        }
        return "redirect:/gerente/tarefas";
    }

    @PostMapping("/tarefas/{id}/status")
    public String atualizarStatus(@PathVariable Long id,
                                  @RequestParam String status,
                                  @RequestParam(required = false) String observacao,
                                  RedirectAttributes redirectAttributes) {
        try {
            tarefaService.atualizarStatus(id, status, observacao);
            redirectAttributes.addFlashAttribute("success", "Status atualizado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/supervisor/tarefas";
    }

    @GetMapping("/tarefas/{id}/deletar")
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
