package com.tcc.orgLimp.repository;

import com.tcc.orgLimp.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findBySupervisorId(Long supervisorId);
    List<Tarefa> findByStatus(Tarefa.Status status);
    List<Tarefa> findBySupervisorIdAndStatus(Long supervisorId, Tarefa.Status status);
}
