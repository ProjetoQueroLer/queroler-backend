
package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.models.Usuario;
import org.springframework.web.multipart.MultipartFile;

public interface UsuarioService {
    UsuarioResponseDto criar(UsuarioRequestDto dto, MultipartFile foto);
    void adicionarDados(Long id, UsuarioDadosComplementarRequest dto);
    UsuarioDadosResponse getDadosDoUsuario(Long id);
    Usuario getUsuario(Long id);
    void atualizar(Long id, UsuarioAtualizadoLeitorRequest dto);
    void atualizar(Long id, UsuarioAtualizadoAdministradorRequest dto);
    void alterarSenha(UsuarioAlterarSenhaRequest dto,String token);
    void excluirPerfil(Long id);
    void adicionarLivro(Long id, Long idLivro, LivroStatus status);
    byte[] buscarFoto();
}
