
package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.dto.AlterarSenhaRequest;

import com.tcc.orgLimp.dto.UsuarioRequest;
import com.tcc.orgLimp.service.UsuarioService;
import com.tcc.orgLimp.dto.AtualizarPerfilSupervisorRequest;
import com.tcc.orgLimp.dto.UsuarioSessao;
import com.tcc.orgLimp.entity.Usuario;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;
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
            org.springframework.validation.BindingResult bindingResult,
            Authentication authentication,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mensagem = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .filter(java.util.Objects::nonNull)
                    .findFirst()
                    .orElse("Verifique os dados informados.");

            redirectAttributes.addFlashAttribute("error", mensagem);
            return "redirect:/gerente/usuarios";
        }

        Usuario usuarioLogadoAntes = usuarioService.buscarPorEmail(authentication.getName());

        boolean atualizandoPropriaConta = usuarioLogadoAntes != null
                && usuarioLogadoAntes.getId().equals(request.getId());

        try {
            usuarioService.salvar(request);

            if (atualizandoPropriaConta) {
                Usuario usuarioAtualizado = usuarioService.buscarPorEmail(request.getEmail());

                session.setAttribute(
                        "usuario", new UsuarioSessao(usuarioAtualizado));
            }

            redirectAttributes.addFlashAttribute(
                    "success", "Usuário salvo com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Erro ao salvar usuário: " + e.getMessage());
        }

        return "redirect:/gerente/usuarios";
    }

    @PostMapping("/gerente/perfil/atualizar")
    public String atualizarPerfilGerente(
            @Valid @ModelAttribute AtualizarPerfilSupervisorRequest request,
            BindingResult bindingResult,
            Authentication authentication,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mensagem = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .filter(java.util.Objects::nonNull)
                    .findFirst()
                    .orElse("Verifique os dados informados.");

            redirectAttributes.addFlashAttribute("error", mensagem);
            return "redirect:/gerente/perfil";
        }

        try {
            usuarioService.atualizarPerfilProprio(
                    authentication.getName(), request);

            Usuario usuarioAtualizado = usuarioService.buscarPorEmail(request.getEmail());

            session.setAttribute(
                    "usuario", new UsuarioSessao(usuarioAtualizado));

            redirectAttributes.addFlashAttribute(
                    "success", "Perfil atualizado com sucesso.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Não foi possível atualizar o perfil. Tente novamente.");
        }

        return "redirect:/gerente/perfil";
    }

    @PostMapping("/gerente/perfil/alterar-senha")
    public String alterarSenhaGerente(
            @Valid @ModelAttribute AlterarSenhaRequest request,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mensagem = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .filter(java.util.Objects::nonNull)
                    .findFirst()
                    .orElse("Verifique os dados da nova senha.");

            redirectAttributes.addFlashAttribute("errorSenha", mensagem);
            return "redirect:/gerente/perfil";
        }

        try {
            Usuario usuario = usuarioService.buscarPorEmail(
                    authentication.getName());

            if (usuario == null
                    || usuario.getPerfil() != Usuario.Perfil.gerente) {
                throw new IllegalStateException(
                        "Esta operação é exclusiva para gerentes.");
            }

            usuarioService.alterarSenha(authentication.getName(), request);

            redirectAttributes.addFlashAttribute(
                    "successSenha", "Senha alterada com sucesso.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorSenha", e.getMessage());

        } catch (Exception e) {
            System.err.println("Erro ao alterar senha do gerente: " + e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "errorSenha",
                    "Não foi possível alterar a senha. Tente novamente.");
        }

        return "redirect:/gerente/perfil";
    }

    @PostMapping("/supervisor/perfil/atualizar")
    public String atualizarPerfilSupervisor(
            @Valid @ModelAttribute AtualizarPerfilSupervisorRequest request,
            BindingResult bindingResult,
            Authentication authentication,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mensagem = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .filter(java.util.Objects::nonNull)
                    .findFirst()
                    .orElse("Verifique os dados informados.");

            redirectAttributes.addFlashAttribute("errorPerfil", mensagem);
            return "redirect:/supervisor/perfil";
        }

        try {
            usuarioService.atualizarPerfilProprio(
                    authentication.getName(), request);

            Usuario usuarioAtualizado = usuarioService.buscarPorEmail(request.getEmail());

            session.setAttribute(
                    "usuario", new UsuarioSessao(usuarioAtualizado));

            redirectAttributes.addFlashAttribute(
                    "successPerfil", "Perfil atualizado com sucesso.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorPerfil", e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorPerfil", "Não foi possível atualizar o perfil. Tente novamente.");
        }

        return "redirect:/supervisor/perfil";
    }

    @PostMapping("/supervisor/perfil/alterar-senha")
    public String alterarSenha(
            @Valid @ModelAttribute AlterarSenhaRequest request,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String mensagem = bindingResult.getFieldErrors().stream()
                    .map(org.springframework.validation.FieldError::getDefaultMessage)
                    .filter(java.util.Objects::nonNull)
                    .findFirst()
                    .orElse("Verifique os dados da nova senha.");

            redirectAttributes.addFlashAttribute("errorSenha", mensagem);
            return "redirect:/supervisor/perfil";
        }

        try {
            usuarioService.alterarSenha(authentication.getName(), request);

            redirectAttributes.addFlashAttribute(
                    "successSenha", "Senha alterada com sucesso.");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorSenha", e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorSenha", "Não foi possível alterar a senha. Tente novamente.");
        }

        return "redirect:/supervisor/perfil";
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