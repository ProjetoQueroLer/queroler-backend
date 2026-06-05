package com.usuario.quero_ler.dtos.leitura;

import java.time.LocalDateTime;
import java.util.List;

import com.usuario.quero_ler.dtos.livro.LivroResumoResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DiarioDeLeituraResponseDto(
		Long id,
		LivroResumoResponseDto livro,
		@Schema(description = "Data e hora de início no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 10:00:00")
		LocalDateTime inicioDaLeitura,
		@Schema(description = "Data e hora de término no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:00:00")
		LocalDateTime terminoDaLeitura,
		List<AcompanhamentoLeituraResponseDto> acompanhamentos,
		Double nota,
		String tituloDaResenha,
		String resenha,
		Boolean spoilers) {
}
