package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.*;
import com.usuario.quero_ler.mappers.UsuarioMapper;
import com.usuario.quero_ler.models.*;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioRepository;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.service.UsuarioService;
import com.usuario.quero_ler.utils.Senhas;
import com.usuario.quero_ler.utils.Cpf;
import com.usuario.quero_ler.utils.Email;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository repository;
    private final UserRepository userRepository;
    private final UsuarioMapper mapper;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final LoginService loginService;
    private final TokenService tokenService;

    @Value("${api.security.token.expiration-minutes:120}")
    private long tokenExpirationMinutes;

    @Transactional
    @Override
    public UsuarioResponseDto criar(UsuarioRequestDto dto, MultipartFile foto, HttpServletResponse response) {

        String emailNormalizado = dto.email().trim().toLowerCase();

        Email.validar(emailNormalizado);

        if (repository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw new EmailJaCadastradoException("O email '" + emailNormalizado + "' já está cadastrado.");
        }

        Cpf.validateOrThrow(dto.cpf());

        String normalizedCpf = Cpf.normalize(dto.cpf());

        if (repository.existsByCpf(normalizedCpf)) {
            throw new CpfJaCadastradoException("CPF já cadastrado.");
        }

        User user = loginService.criar(dto, UsuarioProfile.LEITOR);

        Usuario usuario = mapper.toEntity(dto);
        usuario.setEmail(emailNormalizado);
        usuario = validarFoto(usuario, foto);

        usuario.setUser(user);
        usuario = repository.save(usuario);

        String token = tokenService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(tokenExpirationMinutes))
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return mapper.toResponse(usuario);
    }

    @Override
    public void adicionarDados(UsuarioDadosComplementarRequest dto, MultipartFile foto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = validarFoto(usuario, foto);
        usuario = dto != null ? mapper.complementarCadastro(usuario, dto) : usuario;
        usuario = repository.save(usuario);
    }

    @Override
    public UsuarioResponseDto getDadosDoUsuario() {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        return mapper.toResponse(usuario);
    }

    @Override
    public void atualizar(UsuarioAtualizadoLeitorRequest dto, MultipartFile foto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();

        String novoEmail = dto.email().trim().toLowerCase();

        Optional<Usuario> usuarioComEmail = repository.findByEmailIgnoreCase(novoEmail);

        if (usuarioComEmail.isPresent()
                && !usuarioComEmail.get().getId().equals(usuario.getId())) {
            throw new EmailJaCadastradoException("O email '" + novoEmail + "' já está cadastrado.");
        }

        usuario = validarFoto(usuario, foto);
        usuario = mapper.update(usuario, dto);

        usuario.setEmail(novoEmail);

        User user = usuario.getUser();
        user.setUser(novoEmail);
        userRepository.save(user);

        repository.save(usuario);
    }

    @Override
    public void atualizar(UsuarioAtualizadoAdministradorRequest dto, MultipartFile foto) {
        Usuario usuario = loginService.getUsuarioLogado().getUsuario();
        usuario = validarFoto(usuario, foto);
        usuario = dto != null ? mapper.update(usuario, dto) : usuario;
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
        User user = loginService.getUsuarioLogado();
        if (!Senhas.validarSenhasIguais(dto.senhaAtual(), user.getSenha())) {
            throw new CredenciaisInvalidasException("A senha digitada não corresponde a atual.");
        }
        Senhas.validar(dto.senhaAtual(), user.getSenha());
        String novaSenha = Senhas.gerar(dto.senhaNova());
        if (user.getProfile().equals(UsuarioProfile.ADMINISTRADOR)
                || user.getProfile().equals(UsuarioProfile.MODERADOR)) {
            if (Boolean.FALSE.equals(user.getSenhaTrocada())) {
                user.setSenhaTrocada(true);
            }
        }
        user.setSenha(novaSenha);
        userRepository.save(user);
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
                        " com ID: '" + id + "'."));
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
                    "image/png");

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
