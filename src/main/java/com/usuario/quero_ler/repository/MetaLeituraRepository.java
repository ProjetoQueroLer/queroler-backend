package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.MetaLeitura;
import com.usuario.quero_ler.models.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaLeituraRepository extends JpaRepository<MetaLeitura, Long> {
    boolean existsByUsuarioAndAno(Usuario usuario, Integer ano);

    Optional<MetaLeitura> findByUsuarioAndAno(Usuario usuario, Integer ano);

    void deleteAllByUsuario(Usuario usuario);
}