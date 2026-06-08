package com.usuario.quero_ler.dtos.leitura;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record DiarioDeLeituraRequestDto(

                Long livroId,

                @PastOrPresent(message = "A data de início não pode estar no futuro")
                @Schema(description = "Data e hora de início no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 10:00:00")
                LocalDateTime inicioDaLeitura,

                @PastOrPresent(message = "A data de término não pode estar no futuro")
                @Schema(description = "Data e hora de término no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:00:00")
                LocalDateTime terminoDaLeitura,

                @PositiveOrZero(message = "A quantidade de páginas lidas deve ser positiva ou zero") Integer paginasLidas,

                @Min(value = 0, message = "A nota mínima é 0") @Max(value = 5, message = "A nota máxima é 5") Double nota,

                String tituloDaResenha,

                String resenha,
								Boolean spoiler) {
}
