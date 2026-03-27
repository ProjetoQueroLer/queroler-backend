package com.usuario.quero_ler.dtos.usuario;

import java.time.LocalDate;

public record UsuarioRequestDto(
        String nome,
        String email,
        String confirmarEmail,
        String senha,
        String confirmarSenha,
        String cpf,
        LocalDate dataDeNascimento,
        Boolean checkTermo
) {
}
