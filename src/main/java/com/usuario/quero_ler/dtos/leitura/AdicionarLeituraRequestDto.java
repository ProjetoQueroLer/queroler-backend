package com.usuario.quero_ler.dtos.leitura;

import com.usuario.quero_ler.enums.LeituraStatus;
import jakarta.validation.constraints.NotNull;

public record AdicionarLeituraRequestDto(
        @NotNull Long livroId,
        @NotNull LeituraStatus status) {
}
