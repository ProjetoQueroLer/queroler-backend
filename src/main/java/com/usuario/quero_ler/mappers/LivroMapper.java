package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.dtos.livro.LivroCardResponse;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enuns.LivroStatus;
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

    public LivroCardResponse toCardResponse(Livro livro){
        List<AutorResponse> autorResponses = new ArrayList<>();
        for(Autor autor : livro.getAutores()){
            autorResponses.add(autorMapper.autorResponse(autor));
        }
        String urlCapa= "Capa não cadastrada.";
        if(livro.getCapaDoLivro()!=null){
            urlCapa = "/livros/"+ livro.getId() + "/capa";
        }

        return new LivroCardResponse(
                urlCapa,
                livro.getTitulo(),
                livro.getEditora(),
                livro.getAnoDePublicacao(),
                livro.getNumeroDePaginas(),
                autorResponses
        );
    }

    public LivroTelaLeituraResponse toLivroTelaLeituraResponse(Livro livro, LivroStatus status){
        String urlCapa= "Capa não cadastrada.";
        if(livro.getCapaDoLivro()!=null){
            urlCapa = "/livros/"+ livro.getId() + "/capa";
        }
        return new LivroTelaLeituraResponse(
                livro.getTitulo(),
                status,
                urlCapa
        );
    }
}