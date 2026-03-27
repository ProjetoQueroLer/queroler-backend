package com.usuario.quero_ler.dtos.documento;

import com.usuario.quero_ler.enuns.DocumentoTipo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO de resposta de um documento")
public record DocumentoResponseDto(
        @Schema(description = "Identificador do documento", example = "1")
        Long id,

        @Schema(description = "Título do documento", example = "Política de Segurança da Informação")
        String titulo,

        @Schema(description = "Tipo do documento")
        DocumentoTipo tipo,

        @Schema(description = "Conteúdo do documento",example = "Este documento descreve as políticas de segurança...")
        String conteudo,

        @Schema(description = "Data e hora da última alteração do documento", example = "2026-03-08T11:30:00")
        LocalDateTime ultimaAlteracao
) {
}