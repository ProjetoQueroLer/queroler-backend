package com.usuario.quero_ler.models;

import com.usuario.quero_ler.enums.LeituraStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_leitura")
@NoArgsConstructor
@AllArgsConstructor
public class Leitura {

    @EmbeddedId
    private UsuarioLivroId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeituraStatus status;

    @ManyToOne
    @JoinColumn(name = "usuario_id",  nullable = false)
    @MapsId("usuarioId")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id",  nullable = false)
    @MapsId("livroId")
    private Livro livro;

    @OneToOne(mappedBy = "leitura",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private DiarioDeLeitura diarioDeLeitura;
}
