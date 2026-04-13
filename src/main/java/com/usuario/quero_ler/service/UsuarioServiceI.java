
package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enuns.LivroStatus;
import com.usuario.quero_ler.models.Usuario;

public interface UsuarioServiceI {
    UsuarioResponseDto criar(UsuarioRequestDto dto);
    void adicionarDados(Long id, UsuarioDadosComplementarRequest dto);
    UsuarioDadosResponse getDadosDoUsuario(Long id);
    Usuario getUsuario(Long id);
    void atualizar(Long id, UsuarioAtualizadoLeitorReguest dto);
    void atualizar(Long id, UsuarioAtualizadoAdministradorReguest dto);
    void alterarSenha(Long id, UsuarioAlterarSenhaReguest dto);
    void excluirPerfil(Long id);
    void adicionarLivro(Long id, Long idLivro, LivroStatus status);
}
