package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
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
        @Schema(description = "Data de cadastro no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:30:00")
        LocalDateTime dataDeCadastro,
        List<AutorResponse> autores
){}