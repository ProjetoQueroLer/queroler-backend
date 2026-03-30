package com.usuario.quero_ler.dtos.notificacao;

import java.time.LocalDateTime;

public record NotificacaoResponseDto(
        Long id,
        String notificacao,
        LocalDateTime dataDeCriacao
) {
}
