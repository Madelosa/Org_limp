package com.tcc.orgLimp.controller;

import com.tcc.orgLimp.entity.Tarefa;
import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.repository.UsuarioRepository;
import com.tcc.orgLimp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {

    private final UsuarioService usuarioService;
    private final TarefaService tarefaService;
    private final NotificacaoService notificacaoService;
    private final ConfiguracaoService configuracaoService;
    private final UsuarioRepository usuarioRepository;

    public PageController(UsuarioService usuarioService, TarefaService tarefaService,
            NotificacaoService notificacaoService, ConfiguracaoService configuracaoService,
            UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.tarefaService = tarefaService;
        this.notificacaoService = notificacaoService;
        this.configuracaoService = configuracaoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null)
            return "redirect:/login";

        session.setAttribute("usuario", usuario);

        if (usuario.getPerfil() == Usuario.Perfil.gerente) {
            return "redirect:/gerente/dashboard";
        }
        return "redirect:/supervisor/dashboard";
    }

    // ========== GERENTE ==========

    @GetMapping("/gerente/dashboard")
    public String gerenteDashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("tarefas", tarefaService.listarTodas());
        model.addAttribute("notificacoes", notificacaoService.listarPorDestinatario(usuario.getId()));
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/gerente/dashboard";
    }

    @GetMapping("/gerente/tarefas")
    public String gerenteTarefas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("tarefas", tarefaService.listarTodas());
        model.addAttribute("supervisores", usuarioService.listarSupervisores());
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/gerente/tarefas";
    }

    @GetMapping("/gerente/plano")
    public String gerentePlano(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("tarefas", tarefaService.listarTodas());
        model.addAttribute("supervisores", usuarioService.listarSupervisores());
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/gerente/plano";
    }

    @GetMapping("/gerente/relatorios")
    public String gerenteRelatorios(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Tarefa> tarefas = tarefaService.listarTodas();
        List<Usuario> supervisores = usuarioService.listarSupervisores();

        long totalTarefas = tarefas.size();

        long concluidas = tarefas.stream()
                .filter(t -> t.getStatus() == Tarefa.Status.concluida)
                .count();

        long pendentes = tarefas.stream()
                .filter(t -> t.getStatus() == Tarefa.Status.pendente)
                .count();

        long taxaConclusao = totalTarefas > 0
                ? (concluidas * 100) / totalTarefas
                : 0;

        model.addAttribute("usuario", usuario);
        model.addAttribute("tarefas", tarefas);
        model.addAttribute("supervisores", supervisores);

        model.addAttribute("totalTarefas", totalTarefas);
        model.addAttribute("concluidas", concluidas);
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("taxaConclusao", taxaConclusao);

        model.addAttribute(
                "naoLidas",
                notificacaoService.contarNaoLidas(usuario.getId()));

        return "pages/gerente/relatorios";
    }

    @GetMapping("/gerente/notificacoes")
    public String gerenteNotificacoes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("notificacoes", notificacaoService.listarPorDestinatario(usuario.getId()));
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/gerente/notificacoes";
    }

    @GetMapping("/gerente/usuarios")
    public String gerenteUsuarios(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/gerente/usuarios";
    }

    @GetMapping("/gerente/configuracoes")
    public String gerenteConfiguracoes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("configuracao", configuracaoService.buscar());
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/gerente/configuracoes";
    }

    @GetMapping("/gerente/perfil")
    public String gerentePerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/gerente/perfil";
    }

    // ========== SUPERVISOR ==========

    @GetMapping("/supervisor/dashboard")
    public String supervisorDashboard(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Tarefa> minhasTarefas = tarefaService.listarPorSupervisor(usuario.getId());
        model.addAttribute("usuario", usuario);
        model.addAttribute("tarefas", minhasTarefas);
        model.addAttribute("notificacoes", notificacaoService.listarPorDestinatario(usuario.getId()));
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/supervisor/dashboard";
    }

    @GetMapping("/supervisor/tarefas")
    public String supervisorTarefas(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("tarefas", tarefaService.listarPorSupervisor(usuario.getId()));
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/supervisor/tarefas";
    }

    @GetMapping("/supervisor/notificacoes")
    public String supervisorNotificacoes(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("notificacoes", notificacaoService.listarPorDestinatario(usuario.getId()));
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/supervisor/notificacoes";
    }

    @GetMapping("/supervisor/perfil")
    public String supervisorPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        model.addAttribute("naoLidas", notificacaoService.contarNaoLidas(usuario.getId()));
        return "pages/supervisor/perfil";
    }
}
