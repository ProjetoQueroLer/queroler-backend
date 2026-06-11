package com.usuario.quero_ler.dtos.usuario;

import java.time.LocalDate;

public record UsuarioRequestDto(
        String nome,
        String email,
        String senha,
        String cpf,
        LocalDate dataDeNascimento,
        Boolean checkTermo
) {}