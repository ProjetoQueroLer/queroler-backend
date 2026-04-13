package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;

import java.util.List;

public record LivroDetalhadoResponse(
        String urlCapaDoLivro,
        String titulo,
        String editora,
        String anoDePublicacao,
        Integer numeroDePaginas,
        String idioma,
        String isbn,
        String sinopse,
        List<AutorResponse> autores
){}