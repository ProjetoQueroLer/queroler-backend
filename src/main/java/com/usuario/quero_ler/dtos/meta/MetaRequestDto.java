package com.usuario.quero_ler.dtos.meta;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criação ou atualização de uma meta de leitura")
public record MetaRequestDto(

        @Schema(description = "Ano de referência da meta", example = "2026")
        Integer ano,

        @Schema(description = "Quantidade de livros que se deseja ler no ano", example = "24")
        Integer metaLivrosAno,

        @Schema(description = "Quantidade de livros que se deseja ler por mês", example = "2")
        Integer metaLivrosMes,

        @Schema(description = "Quantidade de páginas que se deseja ler por dia", example = "30")
        Integer metaPaginasDia
) {}