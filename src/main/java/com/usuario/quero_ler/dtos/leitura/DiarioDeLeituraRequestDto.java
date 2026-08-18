package com.usuario.quero_ler.dtos.leitura;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record DiarioDeLeituraRequestDto(

                Long livroId,

                @PastOrPresent
                @Schema(description = "Data e hora de início no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 10:00:00")
                LocalDateTime inicioDaLeitura,

                @PastOrPresent()
                @Schema(description = "Data e hora de término no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:00:00")
                LocalDateTime terminoDaLeitura,

                @PositiveOrZero Integer paginasLidas,

                Double nota,

                String tituloDaResenha,

                String resenha,
								Boolean spoiler) {
}
