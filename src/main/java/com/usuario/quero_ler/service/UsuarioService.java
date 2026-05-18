
package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.models.Usuario;
import org.springframework.web.multipart.MultipartFile;

public interface UsuarioService {
    UsuarioResponseDto criar(UsuarioRequestDto dto, MultipartFile foto);
    void adicionarDados(UsuarioDadosComplementarRequest dto);
    UsuarioDadosResponse getDadosDoUsuario();
    Usuario getUsuario(Long id);
    void atualizar(UsuarioAtualizadoLeitorRequest dto);
    void atualizar(UsuarioAtualizadoAdministradorRequest dto);
    void alterarSenha(UsuarioAlterarSenhaRequest dto);
    void excluirPerfil();
    void adicionarLivro(Long idLivro, LivroStatus status);
    byte[] buscarFoto();
}
