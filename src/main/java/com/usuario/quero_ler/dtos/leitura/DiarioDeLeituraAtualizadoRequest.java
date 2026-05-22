package com.usuario.quero_ler.dtos.leitura;

import java.time.LocalDateTime;

public record DiarioDeLeituraAtualizadoRequest(
        LocalDateTime inicioDaLeitura,
        LocalDateTime terminoDaLeitura,
        Integer paginasLidas,
        Integer nota,
        String tituloDaResenha,
        String resenha) {
}
