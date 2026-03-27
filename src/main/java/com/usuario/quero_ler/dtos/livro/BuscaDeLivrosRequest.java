package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.enuns.TiposDeBusca;

public record BuscaDeLivrosRequest(
        TiposDeBusca tiposDeBusca,
        String valor
) {}