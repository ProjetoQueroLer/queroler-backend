package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.*;
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository repository;
    private final UserRepository userRepository;
    private final UsuarioMapper mapper;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final UsuarioLivroRepository usuarioLivroRepository;
    private final LivroService livroService;
    private final LoginService loginService;

    @Transactional
    @Override
    public UsuarioResponseDto criar(UsuarioRequestDto dto, MultipartFile foto) {
        log.info("UsuarioServiceImpl.criar - iniciando email={}", dto.email());
        Senhas.validarSenhasIguais(dto.senha(), dto.confirmarSenha());
        User user = loginService.criar(dto, UsuarioProfile.LEITOR);

        Usuario usuario = mapper.toEntity(dto);
        usuario = validarFoto(usuario, foto);

        usuario.setUser(user);
        usuario = repository.save(usuario);
        UsuarioResponseDto resp = mapper.toResponse(usuario);
        log.info("UsuarioServiceImpl.criar - concluído id={}", usuario.getId());
        return resp;
    }

    @Override
    public void adicionarDados(UsuarioDadosComplementarRequest dto, MultipartFile foto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = validarFoto(usuario, foto);
        usuario = dto != null ? mapper.complementarCadastro(usuario, dto) : usuario;
        usuario = repository.save(usuario);
        log.info("UsuarioServiceImpl.adicionarDados - concluído id={}", usuario.getId());
    }

    @Override
    public UsuarioDadosResponse getDadosDoUsuario() {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        log.debug("UsuarioServiceImpl.getDadosDoUsuario - id={}", usuario.getId());
        UsuarioDadosResponse resp = mapper.toResponseDados(usuario);
        log.debug("UsuarioServiceImpl.getDadosDoUsuario - concluído id={}", usuario.getId());
        return resp;
    }

    @Override
    public void atualizar(UsuarioAtualizadoLeitorRequest dto, MultipartFile foto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = validarFoto(usuario, foto);
        usuario = (dto != null ? mapper.update(usuario, dto) : usuario);
        usuario = repository.save(usuario);
        log.info("UsuarioServiceImpl.atualizar (leitor) - concluído id={}", usuario.getId());
    }

    @Override
    public void atualizar(UsuarioAtualizadoAdministradorRequest dto, MultipartFile foto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = validarFoto(usuario, foto);
        usuario = dto != null ? mapper.update(usuario, dto) : usuario;
        repository.save(usuario);
        log.info("UsuarioServiceImpl.atualizar (administrador) - concluído id={}", usuario.getId());
    }

    @Override
    public void excluirPerfil() {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        log.info("UsuarioServiceImpl.excluirPerfil - iniciando id={}", usuario.getId());
        if (usuario.getUser().getProfile().equals(UsuarioProfile.LEITOR)) {
            List<UsuarioNotificacao> notificacoes = usuarioNotificacaoRepository.findByUsuarioId(usuario.getId());
            for (UsuarioNotificacao un : notificacoes) {
                usuarioNotificacaoRepository.delete(un);
            }
            repository.delete(usuario);
            log.info("UsuarioServiceImpl.excluirPerfil - concluído id={}", usuario.getId());
        } else {
            log.warn("UsuarioServiceImpl.excluirPerfil - permissão negada id={}", usuario.getId());
            throw new UsuarioSemPermissaoParaAcaoException("Ação não permitida para este usuário.");
        }
    }

    @Override
    public void alterarSenha(UsuarioAlterarSenhaRequest dto) {
        log.info("UsuarioServiceImpl.alterarSenha - iniciando");
        Senhas.validar(dto.senhaNova());
        User user = loginService.getUsuarioLogado();
        if (!Senhas.validarSenhasIguais(dto.senhaAtual(), user.getSenha())) {
            throw new CredenciaisInvalidasException("A senha digitada não corresponde a atual.");
        }
        Senhas.validar(dto.senhaAtual(), user.getSenha());
        String novaSenha = Senhas.gerar(dto.senhaNova());
        if (user.getProfile().equals(UsuarioProfile.ADMINISTRADOR)
                || user.getProfile().equals(UsuarioProfile.MODERADOR)) {
            if (user.getSenhaTrocada() == false) {
                user.setSenhaTrocada(true);
            }
        }
        user.setSenha(novaSenha);
        userRepository.save(user);
    }

    @Override
    public void adicionarLivro(Long idLivro, LivroStatus status) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        log.info("UsuarioServiceImpl.adicionarLivro - iniciando idUsuario={} idLivro={} status={}", usuario.getId(),
                idLivro, status);

        Optional<UsuarioLivro> usuarioLivro = usuarioLivroRepository.findByUsuarioIdAndLivroId(usuario.getId(),
                idLivro);
        if (usuarioLivro.isPresent()) {
            log.warn("UsuarioServiceImpl.adicionarLivro - livro já presente idUsuario={} idLivro={}", usuario.getId(),
                    idLivro);
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
        log.info("UsuarioServiceImpl.adicionarLivro - concluído idUsuario={} idLivro={}", usuario.getId(), idLivro);
    }

    public User getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User user = (User) authentication.getPrincipal();
        log.debug("UsuarioServiceImpl.getUsuarioLogado - usuarioId={}", user.getId());
        return user;
    }

    public Usuario getUsuario(Long id) {
        log.debug("UsuarioServiceImpl.getUsuario - id={}", id);
        return repository.findById(id).orElseThrow(
                () -> new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário" +
                        " com ID: '" + id + "'."));
    }

    @Override
    public byte[] buscarFoto() {
        Usuario usuarioLogado = loginService.getUsuarioLogado().getUsuario();
        log.info("UsuarioServiceImpl.buscarFoto - iniciando id={}", usuarioLogado.getId());

        if (usuarioLogado.getFoto() == null) {
            log.warn("UsuarioServiceImpl.buscarFoto - foto nao cadastrada id={}", usuarioLogado.getId());
            throw new FotoNaoCadastradaException("Foto não cadastrada");
        } else {
            byte[] foto = usuarioLogado.getFoto();
            log.info("UsuarioServiceImpl.buscarFoto - concluído id={} bytes={}", usuarioLogado.getId(), foto.length);
            return foto;
        }
    }

    protected Usuario validarFoto(Usuario usuario, MultipartFile foto) {
        if (foto != null && !foto.isEmpty()) {
            validarFoto(foto);
            try {
                usuario.setFoto(foto.getBytes());
            } catch (IOException e) {
                throw new CapaForaDePadraoException("Erro ao ler imagem" + e);
            }
        }
        return usuario;
    }

    protected void validarFoto(MultipartFile foto) {
        try {
            log.debug("validarFoto - iniciando size={} contentType={}",
                    foto != null ? foto.getSize() : 0,
                    foto != null ? foto.getContentType() : null);

            if (foto == null || foto.isEmpty()) {
                log.debug("validarFoto - arquivo ausente ou vazio");
                return;
            }

            long tamanhoMaximo = 10 * 1024 * 1024;
            if (foto.getSize() > tamanhoMaximo) {
                log.warn("validarFoto - imagem muito grande size={}", foto.getSize());
                throw new CapaForaDePadraoException("Imagem excede o tamanho máximo de 10MB");
            }

            List<String> tiposPermitidos = List.of(
                    "image/jpeg",
                    "image/jpg",
                    "image/png");

            if (foto.getContentType() == null ||
                    !tiposPermitidos.contains(foto.getContentType())) {
                log.warn("validarFoto - tipo inválido contentType={}", foto.getContentType());
                throw new CapaForaDePadraoException("Formato inválido. Use JPG ou PNG");
            }

            BufferedImage imagem = ImageIO.read(foto.getInputStream());
            if (imagem == null) {
                log.warn("validarFoto - arquivo não é uma imagem válida");
                throw new CapaForaDePadraoException("Arquivo enviado não é uma imagem válida");
            }

            log.debug("validarFoto - validação concluída");

        } catch (IOException e) {
            log.error("validarFoto - erro ao processar imagem", e);
            throw new CapaForaDePadraoException("Erro ao processar imagem");
        }
    }
}
