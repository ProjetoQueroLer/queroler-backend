package com.usuario.quero_ler.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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

    @Size(max = 80)
    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Email
    @Size(max = 150)
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Size(min = 11, max = 14)
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Past
    @Column(name = "data_nascimento")
    private LocalDate dataDeNascimento;

    @Column(name = "aceite_termos", nullable = false)
    private Boolean aceitarTermos;

    @Size(max = 80)
    @Column(name = "cidade", length = 80)
    private String cidade;

    @Size(max = 100)
    @Column(name = "estado", length = 100)
    private String estado;

    @Size(max = 100)
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
    private List<UsuarioLivro> livros;
}
