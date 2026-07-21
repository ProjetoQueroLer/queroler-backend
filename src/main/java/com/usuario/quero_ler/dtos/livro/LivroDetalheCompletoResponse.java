package com.usuario.quero_ler.dtos.livro;

import com.usuario.quero_ler.dtos.autor.AutorResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Schema(description = "Detalhamento completo de um livro, incluindo estatísticas e resenhas públicas")
public record LivroDetalheCompletoResponse(

        String urlCapaDoLivro,
        String titulo,
        String editora,
        Year anoDePublicacao,
        Integer numeroDePaginas,
        String idioma,
        String isbn,
        String sinopse,
        @Schema(description = "Data de cadastro no formato dd/MM/yyyy HH:mm:ss", example = "08/03/2026 11:30:00")
        LocalDateTime dataDeCadastro,
        List<AutorResponse> autores,

        @Schema(description = "Média das avaliações do livro")
        Double mediaAvaliacao,

        @Schema(description = "Número total de usuários que avaliaram o livro")
        Long totalAvaliacoes,

        @Schema(description = "Quantidade de usuários que querem ler")
        Long quantidadeQueremLer,

        @Schema(description = "Quantidade de usuários que estão lendo")
        Long quantidadeEstaoLendo,

        @Schema(description = "Quantidade de usuários que já leram")
        Long quantidadeJaLeRAM,

        @Schema(description = "Quantidade de usuários que abandonaram")
        Long quantidadeAbandonaram,

        @Schema(description = "Resenhas públicas do livro")
        List<ResenhaPublicaResponse> resenhas
) {}
