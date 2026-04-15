package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.enuns.LivroStatus;

import java.time.LocalDateTime;

public record LivroTelaLeituraResponse(
        String titulo,
        LivroStatus status,
        String urlCapa,
        LocalDateTime dataDeCadstro
) {
}