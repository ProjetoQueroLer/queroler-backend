package com.usuario.quero_ler.dtos.leitura;

public record AcompanhamentoResponseDto(
        Long id,
        Integer paginaInicial,
        Integer paginaFinal,
        String comentario,
        Long diarioId,
        Long usuarioId) {
}
