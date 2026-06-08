package com.usuario.quero_ler.dtos.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record UsuarioDadosResponse(
        String nome,
        String email,
        @Schema(description = "Data de nascimento no formato dd/MM/yyyy", example = "31/12/1990")
        LocalDate dataDeNascimento,
        String cidade,
        String estado,
        String pais,
        byte[] foto
) {
}
