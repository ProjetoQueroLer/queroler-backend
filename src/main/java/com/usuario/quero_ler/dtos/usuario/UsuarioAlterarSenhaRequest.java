package com.usuario.quero_ler.dtos.usuario;

public record UsuarioAlterarSenhaRequest(
        String senhaAtual,
        String senhaNova
) {}