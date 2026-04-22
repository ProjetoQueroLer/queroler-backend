package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.enums.TiposDeBusca;

public record BuscaDeLivrosRequest(
        TiposDeBusca tiposDeBusca,
        String valor
) {}