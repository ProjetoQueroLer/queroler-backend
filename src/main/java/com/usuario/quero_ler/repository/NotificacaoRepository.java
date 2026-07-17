package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao,Long> {
    void deleteByDataDeCriacaoBefore(LocalDateTime dataDeCorte);
    @Query("""
        SELECT CASE WHEN COUNT(n) > 0 THEN true ELSE false END
        FROM Notificacao n
        WHERE LOWER(n.notificacao) = LOWER(:notificacao)
          AND FUNCTION('DATE', n.dataDeCriacao) = :data
    """)
    boolean existsByDataAndNotificacaoIgnoreCase(
            @Param("data") LocalDate data,
            @Param("notificacao") String notificacao);
}