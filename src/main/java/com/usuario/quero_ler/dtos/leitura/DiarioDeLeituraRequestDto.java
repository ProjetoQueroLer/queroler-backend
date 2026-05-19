package com.usuario.quero_ler.dtos.leitura;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record DiarioDeLeituraRequestDto(

                Long livroId,

                @PastOrPresent(message = "A data de início não pode estar no futuro") LocalDateTime inicioDaLeitura,

                @PastOrPresent(message = "A data de término não pode estar no futuro") LocalDateTime terminoDaLeitura,

                @PositiveOrZero(message = "A quantidade de páginas lidas deve ser positiva ou zero") Integer paginasLidas,

                @Min(value = 0, message = "A nota mínima é 0") @Max(value = 5, message = "A nota máxima é 5") Integer nota,

                String tituloDaResenha,

                String resenha) {
}
