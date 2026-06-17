package com.usuario.quero_ler.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_usuario")
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataDeNascimento;

    @Column(name = "aceite_termos", nullable = false)
    private Boolean aceitarTermos;

    @Column(name = "cidade", length = 80)
    private String cidade;

    @Column(name = "estado", length = 100)
    private String estado;

    @Column(name = "pais", length = 100)
    private String pais;

    @Column(name = "foto", columnDefinition = "BYTEA")
    private byte[] foto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<UsuarioNotificacao> notificacoes;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Leitura> livros;
}
