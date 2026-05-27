package com.usuario.quero_ler.dtos.leitura;

public record AcompanhamentoLeituraResponseDto(
		Long id,
		Integer paginaInicial,
		Integer paginaFinal,
		String comentario) {
}
