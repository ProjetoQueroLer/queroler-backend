
package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.models.Usuario;
import org.springframework.web.multipart.MultipartFile;

public interface UsuarioService {
    UsuarioResponseDto criar(UsuarioRequestDto dto, MultipartFile foto);
    void adicionarDados(UsuarioDadosComplementarRequest dto,MultipartFile foto);
    UsuarioResponseDto getDadosDoUsuario();
    Usuario getUsuario(Long id);
    void atualizar(UsuarioAtualizadoLeitorRequest dto, MultipartFile foto);
    void atualizar(UsuarioAtualizadoAdministradorRequest dto, MultipartFile foto);
    void alterarSenha(UsuarioAlterarSenhaRequest dto);
    void excluirPerfil();
    byte[] buscarFoto();
}
