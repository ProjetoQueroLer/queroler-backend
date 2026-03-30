package com.usuario.quero_ler.models;

import com.usuario.quero_ler.enuns.LivroStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_usuario_livro")
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLivro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LivroStatus status;

    @ManyToOne
    @JoinColumn(name = "usuario_id",  nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id",  nullable = false)
    private Livro livro;
}
