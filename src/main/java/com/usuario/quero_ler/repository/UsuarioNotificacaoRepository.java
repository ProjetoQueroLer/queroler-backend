package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.models.UsuarioNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsuarioNotificacaoRepository extends JpaRepository<UsuarioNotificacao, Long> {
    @Modifying
    @Query(value = """
                INSERT INTO tb_usuario_notificacao (usuario_id, notificacao_id, visualizada)
                SELECT u.id, :notificacaoId, false
                FROM tb_usuario u
            """, nativeQuery = true)
    void enviarParaTodosUsuarios(Long notificacaoId);


    @Query("""
                SELECT un.notificacao
                FROM UsuarioNotificacao un
                WHERE un.usuario.id = :usuarioId
                AND (un.visualizada = false OR un.visualizada IS NULL)
            """)
    List<Notificacao> buscarNotificacoesNaoLidas(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("""
            UPDATE UsuarioNotificacao un
            SET un.visualizada = true,
                un.dataLeitura = CURRENT_TIMESTAMP
            WHERE un.usuario.id = :usuarioId
            AND un.visualizada = false
            """)
    void marcarComoLidas(@Param("usuarioId") Long usuarioId);

    long deleteByNotificacaoDataDeCriacaoBefore(LocalDateTime data);

    List<UsuarioNotificacao> findByUsuarioId(Long usuarioId);
}