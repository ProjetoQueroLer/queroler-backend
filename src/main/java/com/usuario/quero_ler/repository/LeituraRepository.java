package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.Leitura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface LeituraRepository extends JpaRepository<Leitura, Long> {
    Optional<Leitura> findByUsuarioIdAndLivroId(Long usuarioId, Long livroId);

    Optional<Leitura> findByLivro_IdAndUsuario_Id(Long Livroid, Long usuarioId);

    boolean existsByUsuarioIdAndLivroId(Long usuarioId, Long livroId);

    Page<Leitura> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    @Query("SELECT ul.livro FROM Leitura ul WHERE ul.usuario.id = :usuarioId")
    Page<Livro> findLivrosByUsuarioId(Long usuarioId, Pageable pageable);
}
