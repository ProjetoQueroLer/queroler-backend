package com.usuario.quero_ler.dtos.meta;

import com.usuario.quero_ler.dtos.livro.LivroResponse;

import java.util.List;

public record MetaResponseDto(
        Integer ano,
        Integer metaLivrosAno,
        Integer metaLivrosMes,
        Integer metaPaginasDia,
        List<LivroResponse> livros
) {
}