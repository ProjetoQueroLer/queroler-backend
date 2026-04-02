package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;

import java.util.List;

public record LivroCardResponse (
        String urlCapaDoLivro,
        String titulo,
        String editora,
        String anoDePublicacao,
        Integer numeroDePaginas,
        List<AutorResponse> autores
){}