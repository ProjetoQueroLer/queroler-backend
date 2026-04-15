package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.livro.*;
import com.usuario.quero_ler.enuns.LivroIdioma;
import com.usuario.quero_ler.enuns.LivroStatus;
import com.usuario.quero_ler.enuns.TiposDeBusca;
import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.UsuarioLivro;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LivroFixture {
    private static final Long ID = 1L;
    private static final String TITULO = "Clean Code";
    private static final String ISBN = "9780132350884";
    private static final String EDITORA = "Prentice Hall";
    private static final String ANODEPUBLICACAO = "2008";
    private static final Integer NUMERODEPAGINAS = 464;
    private static final LivroIdioma IDIOMA = LivroIdioma.PORTUGUES;
    private static final String SINOPSE = "Um guia sobre boas práticas de programação e escrita de código limpo.";
    private static final byte[] CAPADOLIVRO = carregarImagem();
    private static final List<Autor> AUTORES = new ArrayList<>();
    private static final List<UsuarioLivro> USUARIOS = new ArrayList<>();

    public static BuscaDeLivrosRequest buscaDeLivrosRequest(TiposDeBusca tiposDeBusca){
        return switch (tiposDeBusca){
            case ISBN -> new BuscaDeLivrosRequest(TiposDeBusca.ISBN,ISBN);
            case EDITORA -> new BuscaDeLivrosRequest(TiposDeBusca.EDITORA,EDITORA);
            case TITULO -> new BuscaDeLivrosRequest(TiposDeBusca.TITULO,TITULO);
            case AUTOR -> new BuscaDeLivrosRequest(TiposDeBusca.AUTOR,AutorFixture.entity().getNome());
        };
    }

    public static LivroRequest request(){
        return new LivroRequest(
                TITULO,
                ISBN,
                EDITORA,
                ANODEPUBLICACAO,
                NUMERODEPAGINAS,
                IDIOMA,
                SINOPSE,
                List.of(AutorFixture.request())
        );
    }

    public static Livro entity(){
      List<Autor>autores = new ArrayList<>();
      autores.add(AutorFixture.entity());
        return new Livro(
                ID,
                TITULO,
                ISBN,
                EDITORA,
                ANODEPUBLICACAO,
                NUMERODEPAGINAS,
                IDIOMA,
                SINOPSE,
                null,
                autores,
                null,
                LocalDateTime.now(),
                0

        );
    }
public static Livro entityComCapa(){
      List<Autor>autores = new ArrayList<>();
      autores.add(AutorFixture.entity());
      return new Livro(
                ID,
                TITULO,
                ISBN,
                EDITORA,
                ANODEPUBLICACAO,
                NUMERODEPAGINAS,
                IDIOMA,
                SINOPSE,
                CAPADOLIVRO,
                autores,
                null,
              LocalDateTime.now(),
              0
        );
    }

    public static LivroResponse response(){
        return new LivroResponse(
                ID,
                TITULO,
                ISBN,
                EDITORA,
                ANODEPUBLICACAO,
                NUMERODEPAGINAS,
                IDIOMA,
                SINOPSE,
                "/livros/"+ ID + "/capa",
                List.of(AutorFixture.response())
        );
    }

    public static LivroCardResponse responseCard(){
        return new LivroCardResponse(
                "/livros/"+ ID + "/capa",
                TITULO,
                EDITORA,
                ANODEPUBLICACAO,
                NUMERODEPAGINAS,
                List.of(AutorFixture.response())
        );
    }

    public static LivroTelaLeituraResponse responseTelaDeLeitura(LivroStatus status){
        return new LivroTelaLeituraResponse(
                TITULO,
                status,
                "/livros/"+ ID + "/capa"
        );
    }

    public static LivroDetalhadoResponse responseDetalhado(LivroStatus status){
        return new LivroDetalhadoResponse(
                "/livros/"+ ID + "/capa",
                TITULO,
                EDITORA,
                ANODEPUBLICACAO,
                NUMERODEPAGINAS,
                IDIOMA.name(),
                ISBN,
                SINOPSE,
                List.of(AutorFixture.response())

        );
    }

    private static byte[] carregarImagem() {
        try (InputStream is = LivroFixture.class
                .getClassLoader()
                .getResourceAsStream("capa.jpg")) {

            if (is == null) {
                throw new RuntimeException("Arquivo capa.jpg não encontrado");
            }

            return is.readAllBytes();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar imagem", e);
        }
    }
}