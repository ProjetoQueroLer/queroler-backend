package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.enums.LivroIdioma;
import jakarta.validation.constraints.*;

import java.time.Year;
import java.util.List;

public record LivroRequest(
                @NotBlank String titulo,

                @NotBlank @Pattern(regexp = "\\d{10}|\\d{13}", message = "ISBN deve conter apenas números e ter 10 ou 13 dígitos") String isbn,

                @NotBlank String editora,

                @NotNull(message = "O ano de publicação é obrigatório") @PastOrPresent(message = "O ano de publicação deve ser no passado ou presente") Year anoDePublicacao,

                @NotNull @Positive Integer numeroDePaginas,

                @NotNull LivroIdioma idioma,

                @NotBlank @Size(min = 50) String sinopse,

                @NotEmpty List<AutorRequest> autores) {
}