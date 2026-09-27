package com.tcc.orgLimp.repository;

import com.tcc.orgLimp.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatarioIdOrderByDataDesc(Long destinatarioId);
    List<Notificacao> findByDestinatarioIdAndLidaFalse(Long destinatarioId);
    long countByDestinatarioIdAndLidaFalse(Long destinatarioId);
}
