package com.usuario.quero_ler.dtos.leitura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AcompanhamentoRequestDto(
        @Positive(message = "paginaInicial deve ser um valor positivo.")
        Integer paginaInicial,
        @Positive(message = "paginaFinal deve ser um valor positivo.")
        Integer paginaFinal,
        @NotBlank(message = "comentario é obrigatório.")
        @Size(max = 5000) String comentario) {
}
