package com.usuario.quero_ler.dtos.documento;

import com.usuario.quero_ler.enuns.DocumentoTipo;

public record DocumentoRequestDto(
        String titulo,
        DocumentoTipo tipo,
        String conteudo
) {}