package com.usuario.quero_ler.dtos.notificacao;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record NotificacaoResponseDto(
        Long id,
        String notificacao,
        @Schema(description = "Data e hora de criação no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:30:00")
        LocalDateTime dataDeCriacao,
        @Schema(description = "Indica se a notificação já foi visualizada pelo usuário")
        Boolean visualizada
) {
}
