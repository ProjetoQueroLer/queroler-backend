package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.UsuarioLivro;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiarioDeLeituraRepository extends JpaRepository<DiarioDeLeitura, Long> {

    boolean existsByUsuarioLivro(UsuarioLivro usuarioLivro);

    @Query("SELECT d FROM DiarioDeLeitura d WHERE d.usuarioLivro.id.usuarioId = :usuarioId AND d.usuarioLivro.id.livroId = :livroId")
    Optional<DiarioDeLeitura> findByUsuarioIdAndLivroId(Long usuarioId, Long livroId);
}
