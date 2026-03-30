package com.usuario.quero_ler.dtos.usuario;

public record UsuarioAlterarSenhaReguest(
        String senhaAtual,
        String senhaNova
) {}