package com.tcc.orgLimp.service;

import org.springframework.security.access.AccessDeniedException;
import com.tcc.orgLimp.dto.TarefaRequest;
import com.tcc.orgLimp.entity.Tarefa;
import com.tcc.orgLimp.entity.Usuario;
import com.tcc.orgLimp.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final NotificacaoService notificacaoService;
    private final UsuarioService usuarioService;

    public TarefaService(TarefaRepository tarefaRepository, NotificacaoService notificacaoService,
            UsuarioService usuarioService) {
        this.tarefaRepository = tarefaRepository;
        this.notificacaoService = notificacaoService;
        this.usuarioService = usuarioService;
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<Tarefa> listarPorSupervisor(Long supervisorId) {
        return tarefaRepository.findBySupervisorId(supervisorId);
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id).orElse(null);
    }

    public Tarefa salvar(TarefaRequest request) {
        boolean novaTarefa = request.getId() == null;

        Tarefa tarefa;

        if (novaTarefa) {
            tarefa = new Tarefa();
        } else {
            tarefa = tarefaRepository.findById(request.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Tarefa não encontrada."));
        }

        Usuario supervisor = usuarioService.buscarPorId(
                request.getSupervisorId());

        if (supervisor == null
                || supervisor.getPerfil() != Usuario.Perfil.supervisor
                || !Boolean.TRUE.equals(supervisor.getAtivo())) {
            throw new IllegalArgumentException(
                    "O supervisor selecionado não existe, não está ativo ou não possui perfil de supervisor.");
        }

        tarefa.setTitulo(request.getTitulo());
        tarefa.setLocal(request.getLocal());
        tarefa.setData(request.getData());
        tarefa.setHora(request.getHora());
        tarefa.setPrazo(request.getPrazo());
        tarefa.setSupervisorId(supervisor.getId());
        tarefa.setStatus(
                Tarefa.Status.valueOf(
                        request.getStatus().replace(" ", "_")));
        tarefa.setObservacao(request.getObservacao());

        Tarefa salva = tarefaRepository.save(tarefa);

        if (novaTarefa) {
            notificacaoService.criar(
                    salva.getSupervisorId(),
                    "Nova tarefa atribuída",
                    "Você recebeu a tarefa \"" + salva.getTitulo() + "\".",
                    "tarefa");
        }

        return salva;
    }

    public Tarefa atualizarStatus(
            Long id,
            String novaStatus,
            String observacao,
            Long supervisorId) {
        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);

        if (tarefa == null) {
            return null;
        }

        if (supervisorId == null
                || !supervisorId.equals(tarefa.getSupervisorId())) {
            throw new AccessDeniedException(
                    "Você não tem permissão para alterar esta tarefa.");
        }

        Tarefa.Status statusAnterior = tarefa.getStatus();
        Tarefa.Status novoStatus = Tarefa.Status.valueOf(novaStatus.replace(" ", "_"));

        if (statusAnterior != novoStatus) {
            tarefa.setStatus(novoStatus);
            if (observacao != null) {
                tarefa.setObservacao(observacao);
            }
            Tarefa atualizada = tarefaRepository.save(tarefa);

            // Notificar gerente sobre mudança de status
            Usuario gerente = usuarioService.listarTodos().stream()
                    .filter(u -> u.getPerfil() == Usuario.Perfil.gerente)
                    .findFirst().orElse(null);
            if (gerente != null) {
                notificacaoService.criar(gerente.getId(), "Tarefa atualizada",
                        "A tarefa \"" + tarefa.getTitulo() + "\" mudou de \"" + statusAnterior + "\" para \""
                                + novoStatus + "\".",
                        "status");
            }

            return atualizada;
        }

        return tarefa;
    }

    public void deletar(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tarefa não encontrada."));

        tarefaRepository.delete(tarefa);
    }
}
