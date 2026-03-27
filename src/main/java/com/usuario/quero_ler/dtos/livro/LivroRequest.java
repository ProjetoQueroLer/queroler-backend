package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.enuns.LivroIdioma;
import jakarta.validation.constraints.*;

import java.util.List;

public record LivroRequest(
        @NotBlank
        String titulo,

        @NotBlank
        @Pattern(regexp = "\\d{10}|\\d{13}", message = "ISBN deve conter apenas números e ter 10 ou 13 dígitos")
        String isbn,

        @NotBlank
        String editora,

        @NotBlank
        String anoDePublicacao,

        @NotNull
        Integer numeroDePaginas,

        @NotNull
        LivroIdioma idioma,

        @NotBlank
        @Size(min = 50, message = "A sinopse deve ter no mínimo 50 caracteres")
        String sinopse,

        @NotEmpty
        List<AutorRequest> autores
) {}