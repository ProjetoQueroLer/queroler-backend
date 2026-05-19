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
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.service.UsuarioService;
import com.usuario.quero_ler.utils.Senhas;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
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
        if (repository.existsByCpf(dto.cpf())) {
            throw new LoginJaCadastradoException("CPF já cadastrado.");
        }
        Senhas.validarIguais(dto.senha(), dto.confirmarSenha());
        User user = loginService.criar(dto, UsuarioProfile.LEITOR);
        Usuario usuario = mapper.toEntity(dto);

        if (foto != null && !foto.isEmpty()) {
            validarFoto(foto);
            try {
                usuario.setFoto(foto.getBytes());
            } catch (IOException e) {
                throw new CapaForaDePadraoException("Erro ao ler imagem" + e);
            }
        }
        usuario.setUser(user);
        usuario = repository.save(usuario);
        return mapper.toResponse(usuario);
    }

    @Override
    public void adicionarDados(UsuarioDadosComplementarRequest dto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = mapper.complementarCadastro(usuario, dto);
        usuario = repository.save(usuario);
    }

    @Override
    public UsuarioDadosResponse getDadosDoUsuario() {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        return mapper.toResponseDados(usuario);
    }

    @Override
    public void atualizar(UsuarioAtualizadoLeitorRequest dto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = mapper.update(usuario, dto);
        usuario = repository.save(usuario);
    }

    @Override
    public void atualizar(UsuarioAtualizadoAdministradorRequest dto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = mapper.update(usuario, dto);
        repository.save(usuario);
    }

    @Override
    public void excluirPerfil() {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        if (usuario.getUser().getProfile().equals(UsuarioProfile.LEITOR)) {
            List<UsuarioNotificacao> notificacoes = usuarioNotificacaoRepository.findByUsuarioId(usuario.getId());
            for (UsuarioNotificacao un : notificacoes) {
                usuarioNotificacaoRepository.delete(un);
            }
            repository.delete(usuario);
        } else {
            throw new UsuarioSemPermissaoParaAcaoException("Ação não permitida para este usuário.");
        }
    }

    @Override
    public void alterarSenha(UsuarioAlterarSenhaRequest dto) {
        Senhas.validar(dto.senhaNova());
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        User user = usuario.getUser();
        Senhas.validar(dto.senhaAtual(), user.getSenha());
        String novaSenha = Senhas.gerar(dto.senhaNova());
        user.setSenha(novaSenha);
        user = userRepository.save(user);
    }

    @Override
    public void adicionarLivro(Long idLivro, LivroStatus status) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();

        Optional<UsuarioLivro> usuarioLivro = usuarioLivroRepository.findByUsuarioIdAndLivroId(usuario.getId(), idLivro);
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


    public User getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return (User) authentication.getPrincipal();
    }

    public Usuario getUsuario(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new UsuarioNaoEncontradoException("Não foi encontrado nenhum usuário" +
                        " com ID: '" + id + "'.")
        );
    }

    @Override
    public byte[] buscarFoto() {
        Usuario usuarioLogado = loginService.getUsuarioLogado().getUsuario();

        if (usuarioLogado.getFoto() == null) {
            throw new FotoNaoCadastradaException("Foto não cadastrada");
        } else {
            return usuarioLogado.getFoto();
        }
    }

    protected void validarFoto(MultipartFile foto) {
        try {
            if (foto == null || foto.isEmpty()) {
                return;
            }

            long tamanhoMaximo = 10 * 1024 * 1024;
            if (foto.getSize() > tamanhoMaximo) {
                throw new CapaForaDePadraoException("Imagem excede o tamanho máximo de 10MB");
            }

            List<String> tiposPermitidos = List.of(
                    "image/jpeg",
                    "image/jpg",
                    "image/png"
            );

            if (foto.getContentType() == null ||
                    !tiposPermitidos.contains(foto.getContentType())) {
                throw new CapaForaDePadraoException("Formato inválido. Use JPG ou PNG");
            }

            BufferedImage imagem = ImageIO.read(foto.getInputStream());
            if (imagem == null) {
                throw new CapaForaDePadraoException("Arquivo enviado não é uma imagem válida");
            }

        } catch (IOException e) {
            throw new CapaForaDePadraoException("Erro ao processar imagem");
        }
    }
}
