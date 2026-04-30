package com.usuario.quero_ler.dtos.usuario;

import com.usuario.quero_ler.enums.UsuarioProfile;

import java.time.LocalDate;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        String cpf,
        UsuarioProfile profile,
        LocalDate dataDeNascimento,
        Boolean checkTermo,
        String cidade,
        String estado,
        String pais,
        byte[] foto
) {}