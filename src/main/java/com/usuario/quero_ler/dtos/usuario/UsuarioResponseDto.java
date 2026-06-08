package com.usuario.quero_ler.dtos.usuario;

import com.usuario.quero_ler.enums.UsuarioProfile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        String cpf,
        UsuarioProfile profile,
        @Schema(description = "Data de nascimento no formato dd/MM/yyyy", example = "31/12/1990")
        LocalDate dataDeNascimento,
        Boolean checkTermo,
        String cidade,
        String estado,
        String pais,
        String fotoUrl
) {} 