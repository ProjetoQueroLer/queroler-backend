package com.usuario.quero_ler.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private UsuarioLivro usuarioLivro;
    private LocalDateTime inicioDaLeitura;
    private LocalDateTime terminoDaLeitura;
    private Integer paginasLidas;

    @OneToMany(
            mappedBy = "diarioDeLeitura",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AcompanhamentoDeLeitura> cometarios= new ArrayList<>();
    private int nota;
    private String tituloDaResenha;

    @Column(columnDefinition = "TEXT")
    private String resenha;

    public void adicionarComentario(AcompanhamentoDeLeitura comentario){
        cometarios.add(comentario);
    }
}
