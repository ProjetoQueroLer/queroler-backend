package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AcompanhamentoDeLeituraRepository extends JpaRepository<AcompanhamentoDeLeitura, Long> {
    List<AcompanhamentoDeLeitura> findByDiarioDeLeitura_UsuarioLivro_Livro_Id(Long livroId);

    List<AcompanhamentoDeLeitura> findByDiarioDeLeitura_UsuarioLivro_Usuario_Id(Long usuarioId);

    @Query("select a from AcompanhamentoDeLeitura a " +
            "join fetch a.diarioDeLeitura d " +
            "join fetch d.usuarioLivro ul " +
            "join fetch ul.usuario u " +
            "where ul.livro.id = :livroId")
    List<AcompanhamentoDeLeitura> findByLivroIdWithJoins(@Param("livroId") Long livroId);

    @Query("select a from AcompanhamentoDeLeitura a " +
            "join fetch a.diarioDeLeitura d " +
            "join fetch d.usuarioLivro ul " +
            "join fetch ul.usuario u " +
            "where ul.usuario.id = :usuarioId")
    List<AcompanhamentoDeLeitura> findByUsuarioIdWithJoins(@Param("usuarioId") Long usuarioId);
}
