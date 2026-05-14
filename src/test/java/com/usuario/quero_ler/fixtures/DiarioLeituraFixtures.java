package com.usuario.quero_ler.fixtures;


import java.time.LocalDateTime;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;

public final class DiarioLeituraFixtures {

    private DiarioLeituraFixtures() {}

    public static DiarioDeLeituraRequestDto novoDiarioDeLeitura() {
        return new DiarioDeLeituraRequestDto(
                1L,
                2L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                10,
                4,
                "Titulo",
                "resenha"
        );
    }
}
