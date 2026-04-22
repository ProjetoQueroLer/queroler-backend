
package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.models.Usuario;

public interface UsuarioService {
    UsuarioResponseDto criar(UsuarioRequestDto dto);
    void adicionarDados(Long id, UsuarioDadosComplementarRequest dto);
    UsuarioDadosResponse getDadosDoUsuario(Long id);
    Usuario getUsuario(Long id);
    void atualizar(Long id, UsuárioAtualizadoLeitorRequest dto);
    void atualizar(Long id, UsuarioAtualizadoAdministradorRequest dto);
    void alterarSenha(Long id, UsuarioAlterarSenhaRequest dto);
    void excluirPerfil(Long id);
    void adicionarLivro(Long id, Long idLivro, LivroStatus status);
}
