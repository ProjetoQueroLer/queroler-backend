package com.usuario.quero_ler.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_usuario_notificacao")
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioNotificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "notificacao_id")
    private Notificacao notificacao;

    private Boolean visualizada;

    private LocalDateTime dataLeitura;
}