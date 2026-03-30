package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.models.Livro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LivroMapper{
    private final AutorMapper autorMapper;

    public Livro toEntity(LivroRequest dto){
        Livro livro = new Livro();
        livro.setTitulo(dto.titulo());
        livro.setIsbn(dto.isbn());
        livro.setEditora(dto.editora());
        livro.setAnoDePublicacao(dto.anoDePublicacao());
        livro.setNumeroDePaginas(dto.numeroDePaginas());
        livro.setIdioma(dto.idioma());
        livro.setSinopse(dto.sinopse());
        return livro;
    }

    public LivroResponse toResponse(Livro livro){
        List<AutorResponse> autorResponses = new ArrayList<>();
        for(Autor autor : livro.getAutores()){
            autorResponses.add(autorMapper.autorResponse(autor));
        }
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getEditora(),
                livro.getAnoDePublicacao(),
                livro.getNumeroDePaginas(),
                livro.getIdioma(),
                livro.getSinopse(),
                "/livros/"+ livro.getId() + "/capa",
                autorResponses
        );
    }
}
