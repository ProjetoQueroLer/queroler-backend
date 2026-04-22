package com.usuario.quero_ler.dtos.usuario;

import com.usuario.quero_ler.enums.DocumentoTipo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para cadastro de dados adicionais do documento")
public record DadosAdicionaisRequestDto(

        @Schema(
                description = "Título do dado adicional",
                example = "Política de Segurança da Informação"
        )
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String titulo,

        @Schema(
                description = "Tipo do documento",
                example = "PDF"
        )
        @NotNull(message = "O tipo do documento é obrigatório")
        DocumentoTipo tipo,

        @Schema(
                description = "Conteúdo",
                example = "Este documento descreve as políticas de segurança..."
        )
        @NotBlank(message = "O conteúdo é obrigatório")
        String conteudo
) {
}