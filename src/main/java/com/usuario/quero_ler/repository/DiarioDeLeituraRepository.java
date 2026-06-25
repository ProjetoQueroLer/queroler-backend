package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Leitura;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiarioDeLeituraRepository extends JpaRepository<DiarioDeLeitura, Long> {

    boolean existsByLeitura(Leitura leitura);

    @Query("SELECT d FROM DiarioDeLeitura d WHERE d.leitura.usuario.id = :usuarioId AND d.leitura.livro.id = :livroId")
    Optional<DiarioDeLeitura> findByUsuarioIdAndLivroId(@Param("usuarioId")Long usuarioId, @Param("livroId")Long livroId);
}
