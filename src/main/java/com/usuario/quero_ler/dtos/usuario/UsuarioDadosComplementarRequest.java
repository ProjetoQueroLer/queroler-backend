package com.usuario.quero_ler.dtos.usuario;

public record UsuarioDadosComplementarRequest(
        String cidade,
        String estado,
        String pais,
        byte[] foto
) {}