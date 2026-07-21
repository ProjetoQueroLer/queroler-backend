package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Leitura;

import java.util.List;
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

    @Query("SELECT AVG(d.nota) FROM DiarioDeLeitura d WHERE d.leitura.livro.id = :livroId AND d.nota > 0")
    Optional<Double> avgNotaByLivroId(@Param("livroId") Long livroId);

    @Query("SELECT COUNT(d) FROM DiarioDeLeitura d WHERE d.leitura.livro.id = :livroId AND d.nota > 0")
    Long countAvaliacoesByLivroId(@Param("livroId") Long livroId);

    @Query("SELECT d FROM DiarioDeLeitura d WHERE d.leitura.livro.id = :livroId AND d.compartilhar = 'PUBLICO' AND d.resenha IS NOT NULL")
    List<DiarioDeLeitura> findResenhasPublicasByLivroId(@Param("livroId") Long livroId);
}
