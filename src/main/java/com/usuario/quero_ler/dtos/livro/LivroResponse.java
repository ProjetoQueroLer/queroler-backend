package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.enuns.LivroIdioma;

import java.util.List;

public record LivroResponse(
        Long id,
        String titulo,
        String isbn,
        String editora,
        String anoDePublicacao,
        Integer numeroDePaginas,
        LivroIdioma idioma,
        String sinopse,
        String capaUrl,
        List<AutorResponse> autores

) {
}
