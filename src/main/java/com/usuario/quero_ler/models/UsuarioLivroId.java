package com.usuario.quero_ler.models;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.io.Serializable;
@Setter
@EqualsAndHashCode
@Embeddable
public class UsuarioLivroId implements Serializable {
    private Long usuarioId;
    private Long livroId;

}
