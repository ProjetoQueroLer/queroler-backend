package com.usuario.quero_ler.repository;

import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcompanhamentoDeLeituraRepository extends JpaRepository<AcompanhamentoDeLeitura, Long> {
    List<AcompanhamentoDeLeitura> findByDiarioDeLeitura_UsuarioLivro_Livro_Id(Long livroId);

    List<AcompanhamentoDeLeitura> findByDiarioDeLeitura_UsuarioLivro_Usuario_Id(Long usuarioId);
}
