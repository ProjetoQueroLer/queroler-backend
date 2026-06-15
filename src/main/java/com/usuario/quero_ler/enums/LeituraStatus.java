package com.usuario.quero_ler.enums;

public enum LeituraStatus {
    LIVROS_QUE_QUERO_LER("Livros que quero ler"),
    LIVROS_QUE_ESTOU_LENDO("livros que estou lendo"),
    LIVROS_LIDOS("livros lidos"),
    LIVROS_ABANDONADOS("livros abandonados"),
    RELENDO("relendo");

    private final String status;

    LeituraStatus(String tipo) {
        this.status = tipo;
    }
}