package com.usuario.quero_ler.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_diario_leitura")
@NoArgsConstructor
@AllArgsConstructor
public class DiarioDeLeitura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id"),
            @JoinColumn(name = "livro_id", referencedColumnName = "livro_id")
    })
    private Leitura leitura;
    private LocalDateTime inicioDaLeitura;
    private LocalDateTime terminoDaLeitura;
    private Integer paginasLidas;

    @OneToMany(mappedBy = "diarioDeLeitura", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AcompanhamentoDeLeitura> comentarios = new ArrayList<>();

    @Builder.Default
    private Double nota = 0.0;
    private String tituloDaResenha;

    @Builder.Default
    private Boolean spoiler = true;

    @Column(columnDefinition = "TEXT")
    private String resenha;

    public void adicionarComentario(AcompanhamentoDeLeitura comentario) {
        comentarios.add(comentario);
    }
}
