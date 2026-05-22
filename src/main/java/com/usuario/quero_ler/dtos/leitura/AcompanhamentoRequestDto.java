package com.usuario.quero_ler.dtos.leitura;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AcompanhamentoRequestDto(
        @PositiveOrZero Integer paginaInicial,
        @PositiveOrZero Integer paginaFinal,
        @Size(max = 5000) String comentario) {
}
