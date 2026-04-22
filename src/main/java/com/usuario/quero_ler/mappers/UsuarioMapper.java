package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setCpf(dto.cpf());
        usuario.setDataDeNascimento(dto.dataDeNascimento());
        usuario.setAceitarTermos(dto.checkTermo());
        return usuario;
    }

    public Usuario complementarCadastro(Usuario usuario, UsuarioDadosComplementarRequest dto) {
        usuario.setCidade(dto.cidade());
        usuario.setEstado(dto.estado());
        usuario.setPais(dto.pais());
        return usuario;
    }

    public Usuario update(Usuario usuario, UsuárioAtualizadoLeitorRequest dto) {
        usuario.setNome(dto.nome() != null ? dto.nome() :   usuario.getNome());
        usuario.setEmail(dto.email() != null ? dto.email() : usuario.getEmail());
        usuario.setDataDeNascimento(dto.dataDeNascimento() != null ? dto.dataDeNascimento(): usuario.getDataDeNascimento());
        usuario.setCidade(dto.cidade()!=null ? dto.cidade() : usuario.getCidade());
        usuario.setEstado(dto.estado()!=null? dto.estado() : usuario.getEstado());
        usuario.setPais(dto.pais() !=null? dto.pais() : usuario.getPais());
        usuario.setPais(dto.pais() != null ? dto.pais(): usuario.getPais());
        usuario.setFoto(dto.foto() != null ? dto.foto() : usuario.getFoto());
        return usuario;
    }

    public Usuario update(Usuario usuario, UsuarioAtualizadoAdministradorRequest dto) {
        if (dto.dataDeNascimento() != null) {
            usuario.setDataDeNascimento(dto.dataDeNascimento());
        }
        if (dto.cidade() != null) {
            usuario.setCidade(dto.cidade());
        }
        if (dto.estado() != null) {
            usuario.setEstado(dto.estado());
        }
        if (dto.pais() != null) {
            usuario.setPais(dto.pais());
        }
        if (dto.foto() != null) {
            usuario.setFoto(dto.foto());
        }
        return usuario;
    }

    public UsuarioResponseDto toResponse(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getUser().getProfile(),
                usuario.getDataDeNascimento(),
                usuario.getAceitarTermos(),
                usuario.getCidade(),
                usuario.getEstado(),
                usuario.getPais(),
                usuario.getFoto()
        );
    }

    public UsuarioDadosResponse toResponseDados(Usuario usuario) {
        return new UsuarioDadosResponse(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataDeNascimento(),
                usuario.getCidade(),
                usuario.getEstado(),
                usuario.getPais(),
                usuario.getFoto()
        );
    }
}