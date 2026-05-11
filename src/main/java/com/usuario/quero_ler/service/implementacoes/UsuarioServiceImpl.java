package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.UsuarioJaPossueOLivroException;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioSemPermissaoParaAcaoException;
import com.usuario.quero_ler.mappers.UsuarioMapper;
import com.usuario.quero_ler.models.*;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioRepository;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.service.UsuarioService;
import com.usuario.quero_ler.utils.Senhas;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    private final LoginService login;
    private final UsuarioRepository repository;
    private final UserRepository userRepository;
    private final UsuarioMapper mapper;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final UsuarioLivroRepository usuarioLivroRepository;
    private final LivroService livroService;

    @Transactional
    @Override
    public UsuarioResponseDto criar(UsuarioRequestDto dto) {
        log.info("UsuarioServiceImpl.criar - email={}", dto.email());
        Senhas.validarIguais(dto.senha(), dto.confirmarSenha());
        User user = login.criar(dto, UsuarioProfile.LEITOR);
        Usuario usuario = mapper.toEntity(dto);
        usuario.setUser(user);
        usuario = repository.save(usuario);
        UsuarioResponseDto resp = mapper.toResponse(usuario);
        log.info("Usuario criado id={}", usuario.getId());
        return resp;
    }

    @Override
    public void adicionarDados(Long id, UsuarioDadosComplementarRequest dto) {
        log.info("UsuarioServiceImpl.adicionarDados - id={}", id);
        Usuario usuario = getUsuario(id);
        usuario = mapper.complementarCadastro(usuario, dto);
        usuario = repository.save(usuario);
    }

    @Override
    public UsuarioDadosResponse getDadosDoUsuario(Long id) {
        log.debug("UsuarioServiceImpl.getDadosDoUsuario - id={}", id);
        Usuario usuario = getUsuario(id);
        return mapper.toResponseDados(usuario);
    }

    @Override
    public void atualizar(Long id, UsuarioAtualizadoLeitorRequest dto) {
        log.info("UsuarioServiceImpl.atualizar (leitor) - id={}", id);
        Usuario usuario = getUsuario(id);
        usuario = mapper.update(usuario, dto);
        usuario = repository.save(usuario);
    }

    @Override
    public void atualizar(Long id, UsuarioAtualizadoAdministradorRequest dto) {
        log.info("UsuarioServiceImpl.atualizar (administrador) - id={}", id);
        Usuario usuario = getUsuario(id);
        usuario = mapper.update(usuario, dto);
        repository.save(usuario);
    }

    @Override
    public void excluirPerfil(Long id) {
        log.info("UsuarioServiceImpl.excluirPerfil - id={}", id);
        Usuario usuario = getUsuario(id);
        if (usuario.getUser().getProfile().equals(UsuarioProfile.LEITOR)) {
            List<UsuarioNotificacao> notificacoes = usuarioNotificacaoRepository.findByUsuarioId(id);
            for (UsuarioNotificacao un : notificacoes) {
                usuarioNotificacaoRepository.delete(un);
            }
            repository.delete(usuario);
        } else {
            throw new UsuarioSemPermissaoParaAcaoException("Ação não permitida para este usuário.");
        }
    }

    @Override
    public void alterarSenha(Long id, UsuarioAlterarSenhaRequest dto) {
        log.info("UsuarioServiceImpl.alterarSenha - id={}", id);
        Senhas.validar(dto.senhaNova());
        Usuario usuario = getUsuario(id);
        User user = usuario.getUser();
        Senhas.validar(dto.senhaAtual(), user.getSenha());
        String novaSenha = Senhas.gerar(dto.senhaNova());
        user.setSenha(novaSenha);
        user = userRepository.save(user);
    }

    @Override
    public void adicionarLivro(Long id, Long idLivro, LivroStatus status) {
        log.info("UsuarioServiceImpl.adicionarLivro - usuarioId={} livroId={} status={}", id, idLivro, status);
        Usuario usuario = getUsuario(id);

        Optional<UsuarioLivro> usuarioLivro = usuarioLivroRepository.findByUsuarioIdAndLivroId(id, idLivro);
        if (usuarioLivro.isPresent()) {
            throw new UsuarioJaPossueOLivroException("O usuario já possue o livro na estante.");
        }

        Livro livro = livroService.buscar(idLivro);

        UsuarioLivroId usuarioLivroId = new UsuarioLivroId();
        usuarioLivroId.setUsuarioId(usuario.getId());
        usuarioLivroId.setLivroId(livro.getId());

        UsuarioLivro novoUsuarioLivro = new UsuarioLivro();
        novoUsuarioLivro.setId(usuarioLivroId);
        novoUsuarioLivro.setUsuario(usuario);
        novoUsuarioLivro.setLivro(livro);
        novoUsuarioLivro.setStatus(status);
        usuarioLivroRepository.save(novoUsuarioLivro);
    }

    public Usuario getUsuario(Long id) {
        log.debug("UsuarioServiceImpl.getUsuario - id={}", id);
        return repository.findById(id).orElseThrow(
                () -> new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário" +
                        " com ID: '" + id + "'."));
    }
}
