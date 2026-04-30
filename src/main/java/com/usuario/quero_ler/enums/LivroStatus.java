package com.usuario.quero_ler.enums;

public enum LivroStatus {
    LIVROS_QUE_QUERO_LER("Livros que quero ler"),
    LIVROS_QUE_ESTOU_LENDO("livros que estou lendo"),
    LIVROS_LIDOS("livros lidos"),
    LIVROS_ABANDONADOS("livros abandonados");

    private final String status;

    LivroStatus(String tipo) {
        this.status = tipo;
    }
}