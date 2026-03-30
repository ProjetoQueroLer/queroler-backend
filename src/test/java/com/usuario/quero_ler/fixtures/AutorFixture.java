package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.models.Autor;

public class AutorFixture {
    private static final Long ID = 1L;
    private static final String NOME = "Robert C. Martin";

    public static AutorRequest request() {
        return new AutorRequest(NOME);
    }

    public static Autor entity() {
        return new Autor(ID, NOME, null);
    }

    public static AutorResponse response() {
        return new AutorResponse(ID, NOME);
    }
}
