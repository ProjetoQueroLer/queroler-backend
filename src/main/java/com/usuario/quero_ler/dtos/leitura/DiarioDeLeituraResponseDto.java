package com.usuario.quero_ler.dtos.leitura;

import java.time.LocalDateTime;
import java.util.List;

import com.usuario.quero_ler.dtos.livro.LivroResumoResponseDto;

public record DiarioDeLeituraResponseDto(
		Long id,
		LivroResumoResponseDto livro,
		LocalDateTime inicioDaLeitura,
		LocalDateTime terminoDaLeitura,
		List<AcompanhamentoLeituraResponseDto> acompanhamentos,
		Double nota,
		String tituloDaResenha,
		String resenha,
		Boolean spoilers) {
}
