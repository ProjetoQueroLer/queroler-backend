package com.usuario.quero_ler.models;

import com.usuario.quero_ler.enuns.LivroIdioma;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_livros")
@NoArgsConstructor
@AllArgsConstructor
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false)
    private String titulo;
    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;
    @Column(name = "editora", nullable = false)
    private String editora;
    @Column(name = "ano_de_publicacao", nullable = false)
    private String anoDePublicacao;
    @Column(name = "numero_de_paginas", nullable = false)
    private Integer numeroDePaginas;
    @Enumerated(EnumType.STRING)
    @Column(name = "idioma", nullable = false)
    private LivroIdioma idioma;
    @Column(name = "sinopse", nullable = false)
    private String sinopse;

    @Column(name = "capa")
    private byte[] capaDoLivro;

    @ManyToMany
    @JoinTable(
            name = "tb_livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor>autores = new ArrayList<>();

    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY)
    private List<UsuarioLivro> usuarios = new ArrayList<>();


    @Setter(AccessLevel.NONE)
    @Column(name = "data_de_cadastro",nullable = false)
    private LocalDateTime dataDeCadastro = LocalDateTime.now();

    @Setter(AccessLevel.NONE)
    @Column(name = "quantidade_de_uso")
    private Integer quantidadeDeUso;


    public void adicionarAutor(Autor autor){
        autores.add(autor);
    }

    public void computarAdicao(){
        quantidadeDeUso++;
    }
}
