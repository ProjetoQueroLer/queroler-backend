package com.usuario.quero_ler.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tb_acompanhamento_leitura")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcompanhamentoDeLeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int paginaInicial;
    private int paginaFinal;
    private String comentario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diario_leitura_id")
    private DiarioDeLeitura diarioDeLeitura;
}
