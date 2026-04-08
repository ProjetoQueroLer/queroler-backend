package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.models.UsuarioLivroId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioLivroRepository extends JpaRepository<UsuarioLivro, UsuarioLivroId> {
    Optional<UsuarioLivro> findByUsuarioIdAndLivroId(Long usuarioId, Long livroId);

    Optional<UsuarioLivro> findByUsuario_IdAndLivro_Isbn(Long usuarioId, String isbn);

    boolean existsByUsuarioIdAndLivroId(Long usuarioId, Long livroId);

    Page<UsuarioLivro> findAllByUsuarioId(Long usuarioId, Pageable pageable);

}
