package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao,Long> {
    int deleteByDataDeCriacaoBefore(LocalDateTime dataDeCorte);
}