package com.usuario.quero_ler.dtos.usuario;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(
        @Size(max = 80)
        String nome,
        @Email
        @Size(max = 150)
        String email,
        String senha,
        @Size(min = 11, max = 14)
        String cpf,
        @Past
        LocalDate dataDeNascimento,
        Boolean checkTermo
) {}