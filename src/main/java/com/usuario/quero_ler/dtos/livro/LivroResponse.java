package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.enums.LivroIdioma;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public record LivroResponse(
        Long id,
        String titulo,
        String isbn,
        String editora,
        Year anoDePublicacao,
        Integer numeroDePaginas,
        LivroIdioma idioma,
        String sinopse,
        String capaUrl,
        LocalDateTime dataDeCadastro,
        List<AutorResponse> autores

) {
}
