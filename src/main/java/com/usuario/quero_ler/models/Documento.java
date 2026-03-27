package com.usuario.quero_ler.models;

import com.usuario.quero_ler.enuns.DocumentoTipo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_documento")
@NoArgsConstructor
@AllArgsConstructor
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private DocumentoTipo tipo;

    @Column(name = "conteudo", nullable = false)
    private String conteudo;
    private LocalDateTime ultimaAlteracao;
}