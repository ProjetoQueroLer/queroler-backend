package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    java.util.Optional<Usuario> findByIdAndExcluidoFalse(Long id);
}