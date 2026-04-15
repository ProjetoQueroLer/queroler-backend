package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.enuns.LivroStatus;

import java.util.List;

public record LivroTelaLeituraResponse(
        String titulo,
        LivroStatus status,
        String urlCapa
){}