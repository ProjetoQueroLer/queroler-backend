package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.livro.LivroCardResponse;
import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enuns.LivroStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EstanteDoUsuarioServiceI {
    void adicionar(Long idUsuario, Long idLivro);

    Page<LivroTelaLeituraResponse>lista(Long id, Pageable pageable);

    void  mudarStatus(Long idUsuario, String isbn, LivroStatus status);
}
