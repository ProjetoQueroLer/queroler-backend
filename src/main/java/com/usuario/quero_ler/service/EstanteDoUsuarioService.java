package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enums.LivroStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstanteDoUsuarioService {
    void adicionar(Long idUsuario, Long idLivro);

    Page<LivroTelaLeituraResponse>lista(Long id, Pageable pageable);

    void  mudarStatus(Long idUsuario, String isbn, LivroStatus status);
}
