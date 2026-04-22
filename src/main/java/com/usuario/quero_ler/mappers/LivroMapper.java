package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import com.usuario.quero_ler.dtos.livro.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.models.Livro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getEditora(),
                livro.getAnoDePublicacao(),
                livro.getNumeroDePaginas(),
                livro.getIdioma(),
                livro.getSinopse(),
                getUrlFoto(livro),
                livro.getDataDeCadastro(),
                getAutoresResponse(livro)

        );
    }

    public LivroCardResponse toCardResponse(Livro livro){
        return new LivroCardResponse(
                getUrlFoto(livro),
                livro.getTitulo(),
                livro.getEditora(),
                livro.getAnoDePublicacao(),
                livro.getNumeroDePaginas(),
                livro.getDataDeCadastro(),
                getAutoresResponse(livro)
        );
    }

    public LivroTelaLeituraResponse toLivroTelaLeituraResponse(Livro livro, LivroStatus status){
         return new LivroTelaLeituraResponse(
                livro.getTitulo(),
                status,
                getUrlFoto(livro),
                 livro.getDataDeCadastro()
        );
    }

    public LivroDetalhadoResponse toLivroDetalhadoResponse(Livro livro){
        return new LivroDetalhadoResponse(
                getUrlFoto(livro),
                livro.getTitulo(),
                livro.getEditora(),
                livro.getAnoDePublicacao(),
                livro.getNumeroDePaginas(),
                livro.getIdioma().name(),
                livro.getIsbn(),
                livro.getSinopse(),
                livro.getDataDeCadastro(),
                getAutoresResponse(livro)
        );
    }

    protected String getUrlFoto(Livro livro){
        String urlCapa= "Capa não cadastrada.";
        if(livro.getCapaDoLivro()!=null){
            urlCapa = "/livros/"+ livro.getId() + "/capa";
        }
        return urlCapa;
    }

    protected List<AutorResponse> getAutoresResponse(Livro livro){
        List<AutorResponse> autorResponses = new ArrayList<>();
        for(Autor autor : livro.getAutores()){
            autorResponses.add(autorMapper.autorResponse(autor));
        }
        return autorResponses;
    }
}