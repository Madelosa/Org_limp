package com.tcc.orgLimp.repository;

import com.tcc.orgLimp.entity.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
    Optional<Configuracao> findFirstByOrderByIdAsc();
}
