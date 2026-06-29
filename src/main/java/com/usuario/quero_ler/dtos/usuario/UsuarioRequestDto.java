package com.usuario.quero_ler.dtos.usuario;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record UsuarioRequestDto(
        @Size(max = 80)
        String nome,
        @NotBlank
        @Size(max = 256)
        @Pattern(
                regexp = "^$|^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Informe um endereço de e-mail válido"
        )
        String email,
        String senha,
        @Size(min = 11, max = 14)
        String cpf,
        @Past
        LocalDate dataDeNascimento,
        Boolean checkTermo
) {}