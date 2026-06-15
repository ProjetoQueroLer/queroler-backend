package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.enums.LeituraStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record LivroTelaLeituraResponse(
        String titulo,
        LeituraStatus status,
        String urlCapa,
        @Schema(description = "Data de cadastro no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:30:00")
        LocalDateTime dataDeCadastro
) {
} 