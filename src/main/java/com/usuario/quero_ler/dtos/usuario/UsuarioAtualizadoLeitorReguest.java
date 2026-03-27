package com.usuario.quero_ler.dtos.usuario;

import java.time.LocalDate;

public record UsuarioAtualizadoLeitorReguest(
        String nome,
        String email,
        LocalDate dataDeNascimento,
        String cidade,
        String estado,
        String pais,
        byte[] foto
) {}