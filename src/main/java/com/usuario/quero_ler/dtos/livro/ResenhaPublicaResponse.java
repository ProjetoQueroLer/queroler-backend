package com.usuario.quero_ler.dtos.livro;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resenha pública de um livro feita por um leitor")
public record ResenhaPublicaResponse(
        @Schema(description = "Nome do autor da resenha")
        String nomeDoAutor,

        @Schema(description = "Título da resenha", example = "Uma leitura incrível")
        String tituloDaResenha,

        @Schema(description = "Texto da resenha")
        String resenha,

        @Schema(description = "Indica se a resenha contém spoiler")
        Boolean spoiler,

        @Schema(description = "Avaliação do livro (0 a 5)")
        Double nota,

        @Schema(description = "Data da avaliação no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:30:00")
        LocalDateTime data
) {}
