package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.models.Autor;

public interface AutorServiceI {
    Autor criar(AutorRequest dto);
}
