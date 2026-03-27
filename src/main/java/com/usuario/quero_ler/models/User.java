package com.usuario.quero_ler.models;

import com.usuario.quero_ler.enuns.UsuarioProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login", nullable = false, unique = true)
    private String user;

    @Size(min = 8)
    @Column(name = "senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private UsuarioProfile profile;

    @OneToOne(mappedBy = "user")
    private Usuario usuario;
}