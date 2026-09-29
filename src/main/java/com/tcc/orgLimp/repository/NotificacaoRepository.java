package com.tcc.orgLimp.repository;

import com.tcc.orgLimp.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    @Query("SELECT n FROM Notificacao n WHERE n.destinatarioId = :destinatarioId ORDER BY n.data DESC")
    List<Notificacao> findByDestinatarioIdOrderByDataDesc(@Param("destinatarioId") Long destinatarioId);

    @Query("SELECT n FROM Notificacao n WHERE n.destinatarioId = :destinatarioId AND n.lida = false")
    List<Notificacao> findByDestinatarioIdAndLidaFalse(@Param("destinatarioId") Long destinatarioId);

    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.destinatarioId = :destinatarioId AND n.lida = false")
    long countByDestinatarioIdAndLidaFalse(@Param("destinatarioId") Long destinatarioId);
}
