package com.usuario.quero_ler.dtos.leitura;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record LivroAcompanhamentoResponseDto(
		Long diarioId,
		Long livroId,
		String titulo,
		String urlCapa,
		List<AutorResponse> autores,
		@Schema(description = "Data de início da leitura no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 10:00:00")
		LocalDateTime inicioDaLeitura) {
}
