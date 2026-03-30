package com.usuario.quero_ler.enuns;

public enum UsuarioProfile {
    LEITOR("Leitor"),
    ADMINISTRADOR("Administrador"),
    MODERADOR("Moderador");

    private final String perfil;

    UsuarioProfile(String perfil) {
        this.perfil = perfil;
    }
}