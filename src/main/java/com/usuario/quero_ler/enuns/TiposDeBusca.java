package com.usuario.quero_ler.enuns;

public enum TiposDeBusca {
    TITULO("Titulo"),
    AUTOR("Autor"),
    EDITORA("Editora"),
    ISBN("Isbn");

    private String tipo;

    TiposDeBusca(String tipo) {
        this.tipo = tipo;
    }
}