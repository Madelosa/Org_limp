package com.tcc.orgLimp.service;

import com.tcc.orgLimp.entity.Notificacao;
import com.tcc.orgLimp.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public Notificacao criar(Long destinatarioId, String titulo, String mensagem, String tipo) {
        Notificacao notificacao = new Notificacao(destinatarioId, titulo, mensagem, tipo);
        return notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> listarPorDestinatario(Long destinatarioId) {
        return notificacaoRepository.findByDestinatarioIdOrderByDataDesc(destinatarioId);
    }

    public long contarNaoLidas(Long destinatarioId) {
        return notificacaoRepository.countByDestinatarioIdAndLidaFalse(destinatarioId);
    }

    public void marcarComoLida(Long id) {
        notificacaoRepository.findById(id).ifPresent(n -> {
            n.setLida(true);
            notificacaoRepository.save(n);
        });
    }

    public void marcarTodasComoLidas(Long destinatarioId) {
        List<Notificacao> naoLidas = notificacaoRepository.findByDestinatarioIdAndLidaFalse(destinatarioId);
        naoLidas.forEach(n -> n.setLida(true));
        notificacaoRepository.saveAll(naoLidas);
    }
}
