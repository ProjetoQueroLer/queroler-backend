package com.usuario.quero_ler.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_notificacao")
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_de_criacao", nullable = false)
    private LocalDateTime dataDeCriacao;
    @Column(name = "notificacao", nullable = false)
    private String notificacao;
}