package com.tcc.orgLimp.service;

import com.tcc.orgLimp.dto.TarefaRequest;
import com.tcc.orgLimp.entity.Notificacao;
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

    public TarefaService(TarefaRepository tarefaRepository, NotificacaoService notificacaoService, UsuarioService usuarioService) {
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
        Tarefa tarefa = new Tarefa();
        tarefa.setId(request.getId());
        tarefa.setTitulo(request.getTitulo());
        tarefa.setLocal(request.getLocal());
        tarefa.setData(request.getData());
        tarefa.setHora(request.getHora());
        tarefa.setPrazo(request.getPrazo());
        tarefa.setSupervisorId(request.getSupervisorId());
        tarefa.setStatus(Tarefa.Status.valueOf(request.getStatus().replace(" ", "_")));
        tarefa.setObservacao(request.getObservacao());

        Tarefa salva = tarefaRepository.save(tarefa);

        // Notificar supervisor sobre nova tarefa
        if (request.getId() == null) {
            notificacaoService.criar(salva.getSupervisorId(), "Nova tarefa atribuída",
                    "Você recebeu a tarefa \"" + salva.getTitulo() + "\".", "tarefa");
        }

        return salva;
    }

    public Tarefa atualizarStatus(Long id, String novaStatus, String observacao) {
        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa == null) return null;

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
                        "A tarefa \"" + tarefa.getTitulo() + "\" mudou de \"" + statusAnterior + "\" para \"" + novoStatus + "\".", "status");
            }

            return atualizada;
        }

        return tarefa;
    }

    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }
}
