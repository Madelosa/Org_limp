package com.tcc.orgLimp.service;

import com.tcc.orgLimp.dto.ConfiguracaoRequest;
import com.tcc.orgLimp.entity.Configuracao;
import com.tcc.orgLimp.repository.ConfiguracaoRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;

    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }

    public Configuracao buscar() {
        return configuracaoRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    Configuracao nova = new Configuracao();
                    nova.setEmpresa("Minha Empresa");
                    nova.setEmail("gestao@empresa.com");
                    nova.setNotificarEmail(true);
                    nova.setNotificarWhatsApp(true);
                    return configuracaoRepository.save(nova);
                });
    }

    public Configuracao salvar(ConfiguracaoRequest request) {
        Configuracao configuracao = buscar();
        configuracao.setEmpresa(request.getEmpresa());
        configuracao.setEmail(request.getEmail());
        configuracao.setWhatsapp(request.getWhatsapp());
        configuracao.setNotificarEmail(request.getNotificarEmail());
        configuracao.setNotificarWhatsApp(request.getNotificarWhatsApp());
        return configuracaoRepository.save(configuracao);
    }
}
