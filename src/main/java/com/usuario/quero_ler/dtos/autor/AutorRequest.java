package com.usuario.quero_ler.dtos.autor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AutorRequest(
        @NotBlank
        @Size(min = 3, max = 80)
        String nome
) {
}
