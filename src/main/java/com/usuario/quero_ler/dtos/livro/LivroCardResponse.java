package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

public record LivroCardResponse (
        String urlCapaDoLivro,
        String titulo,
        String editora,
        Year anoDePublicacao,
        Integer numeroDePaginas,
        LocalDateTime dataDeCadastro,
        List<AutorResponse> autores
){}