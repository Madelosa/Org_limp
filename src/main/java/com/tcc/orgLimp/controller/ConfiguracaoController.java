package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.ConfiguracaoRequest;
import com.tcc.orgLimp.service.ConfiguracaoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    public ConfiguracaoController(ConfiguracaoService configuracaoService) {
        this.configuracaoService = configuracaoService;
    }

    @PostMapping("/configuracoes/salvar")
    public String salvar(@ModelAttribute ConfiguracaoRequest request, RedirectAttributes redirectAttributes) {
        try {
            configuracaoService.salvar(request);
            redirectAttributes.addFlashAttribute("success", "Configurações salvas com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar configurações: " + e.getMessage());
        }
        return "redirect:/gerente/configuracoes";
    }
}
