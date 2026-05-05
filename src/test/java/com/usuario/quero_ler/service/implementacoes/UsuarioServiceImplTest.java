package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioSemPermissaoParaAcaoException;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.UsuarioMapper;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioRepository;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private LoginService loginService;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    @Mock
    private UsuarioLivroRepository usuarioLivroRepository;

    @Mock
    private LivroService livroServiceI;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve criar um usuário com sucesso.")
    void deveCriarUmUsuarioComSucesso() {
        UsuarioRequestDto dto = UserFixture.requestDto();
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadePrincipal(user);
        UsuarioResponseDto response = UserFixture.response(usuario);

        when(loginService.criar(dto, UsuarioProfile.LEITOR)).thenReturn(user);
        when(mapper.toEntity(dto)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(response);

        UsuarioResponseDto resposta = service.criar(dto);

        assertNotNull(resposta.id());
        assertEquals(dto.nome(), resposta.nome());
        assertEquals(dto.email(), resposta.email());
        assertEquals(dto.cpf(), resposta.cpf());
        assertEquals(dto.dataDeNascimento(), resposta.dataDeNascimento());
    }

    @Test
    @DisplayName("Deve complementar o cadastro do usuario;")
    void deveAdicionarDadosComplementares() {
        UsuarioDadosComplementarRequest dto = UserFixture.requestDadosComplementares();
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuarioComDadosBasicos = UserFixture.entidadePrincipal(user);
        Long id = usuarioComDadosBasicos.getId();
        Usuario usuarioComDadosCompletos = UserFixture.entidadeCompleta(user);

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuarioComDadosBasicos));
        when(mapper.complementarCadastro(usuarioComDadosBasicos, dto)).thenReturn(usuarioComDadosCompletos);
        when(repository.save(usuarioComDadosCompletos)).thenReturn(usuarioComDadosCompletos);

        service.adicionarDados(id, dto);

        verify(repository).findByIdAndExcluidoFalse(id);
    }

    @Test
    @DisplayName("Deve retornar dados do usuário")
    void deveRetornarDadosDoUsuario() {
        User user = UserFixture.userEntity(UsuarioProfile.MODERADOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        Long id = usuario.getId();
        UsuarioDadosResponse response = UserFixture.responseDados(usuario);

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuario));
        when(mapper.toResponseDados(usuario)).thenReturn(response);

        UsuarioDadosResponse resposta = service.getDadosDoUsuario(id);

        assertEquals(usuario.getNome(), resposta.nome());
        assertEquals(usuario.getEmail(), resposta.email());
        assertEquals(usuario.getDataDeNascimento(), resposta.dataDeNascimento());
        assertEquals(usuario.getCidade(), resposta.cidade());
        assertEquals(usuario.getEstado(), resposta.estado());
        assertEquals(usuario.getPais(), resposta.pais());
        assertEquals(usuario.getFoto(), resposta.foto());
    }

    @Test
    @DisplayName("Deve atualizar um perfil de leitor.")
    void deveAtualizarUmPerfilLeitor() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        Long id = usuario.getId();
        UsuarioAtualizadoLeitorRequest atualizacoes = new UsuarioAtualizadoLeitorRequest(
                "Nome atualizado", "emailAtual@gmail.com", LocalDate.of(1978, 9, 12),
                null, "cidade atualizada", null, null);

        Usuario usuarioAtualizado = UserFixture.atualizar(usuario, atualizacoes);

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuario));
        when(mapper.update(usuario, atualizacoes)).thenReturn(usuarioAtualizado);
        when(repository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado);

        service.atualizar(id, atualizacoes);

        verify(mapper).update(usuario, atualizacoes);
        verify(repository).save(usuarioAtualizado);

    }

    @Test
    @DisplayName("Deve atualizar um perfil de administrador ou moderador .")
    void deveAtualizarUmPerfilAdministradorOuModerador() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        Long id = usuario.getId();
        UsuarioAtualizadoAdministradorRequest atualizacoes = new UsuarioAtualizadoAdministradorRequest(
                LocalDate.of(1978, 9, 12), null, "cidade atualizada", null, null);
        Usuario usuarioAtualizado = UserFixture.atualizar(usuario, atualizacoes);

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuario));
        when(mapper.update(usuario, atualizacoes)).thenReturn(usuarioAtualizado);
        when(repository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado);

        service.atualizar(id, atualizacoes);

        verify(mapper).update(usuario, atualizacoes);
        verify(repository).save(usuarioAtualizado);
    }

    @Test
    @DisplayName("Deve excluir o perfil com sucesso.")
    void deveExcluirPerfilComSucesso() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        Long id = usuario.getId();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuario));
        when(repository.save(usuario)).thenReturn(usuario);

        service.excluirPerfil(id);

        assertTrue(usuario.getExcluido());
        assertNotNull(usuario.getDataExclusao());
        assertTrue(usuario.getUser().getExcluido());
        assertNotNull(usuario.getUser().getDataExclusao());

        verify(repository).findByIdAndExcluidoFalse(id);
        verify(repository).save(usuario);
    }

    @Test
    @DisplayName("Deve permitir ADMIN excluir perfil de LEITOR.")
    void devePermitirAdminExcluirPerfilDeLeitor() {
        User admin = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        User leitor = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuarioLeitor = UserFixture.entidadeCompleta(leitor);
        Long id = usuarioLeitor.getId();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities()));

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuarioLeitor));
        when(repository.save(usuarioLeitor)).thenReturn(usuarioLeitor);

        service.excluirPerfil(id);

        assertTrue(usuarioLeitor.getExcluido());
        assertTrue(usuarioLeitor.getUser().getExcluido());
        verify(repository).save(usuarioLeitor);
    }

    @Test
    @DisplayName("Deve negar LEITOR tentando excluir perfil de ADMIN.")
    void deveNegarLeitorExcluirPerfilDeAdmin() {
        User leitor = UserFixture.userEntity(UsuarioProfile.LEITOR);
        User admin = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        Usuario usuarioAdmin = UserFixture.entidadeCompleta(admin);
        Long id = usuarioAdmin.getId();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(leitor, null, leitor.getAuthorities()));

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuarioAdmin));

        UsuarioSemPermissaoParaAcaoException exception = assertThrows(UsuarioSemPermissaoParaAcaoException.class,
                () -> service.excluirPerfil(id));

        assertEquals("Ação não permitida para este usuário.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve alterar senha com sucesso.")
    void deveAlterarSenhaComSucesso() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        Long id = usuario.getId();
        UsuarioAlterarSenhaRequest dto = new UsuarioAlterarSenhaRequest("Teste123&", "Alterado253$");

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuario));

        service.alterarSenha(id, dto);

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Deve retornar um usuário com sucesso.")
    void deveRetornarUmUsuarioComSucesso() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);
        Long id = usuario.getId();

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.of(usuario));

        Usuario resposta = service.getUsuario(id);

        assertNotNull(resposta.getId());
        assertEquals(usuario.getNome(), resposta.getNome());
        assertEquals(usuario.getCpf(), resposta.getCpf());
        assertEquals(usuario.getDataDeNascimento(), resposta.getDataDeNascimento());
        assertEquals(usuario.getEmail(), resposta.getEmail());
        assertEquals(usuario.getCpf(), resposta.getCpf());
        assertEquals(usuario.getCidade(), resposta.getCidade());
        assertEquals(usuario.getEstado(), resposta.getEstado());
        assertEquals(usuario.getPais(), resposta.getPais());
    }

    @Test
    @DisplayName("Deve lançar excessão ao buscar usuario por id e não encontrar")
    void deveLancarExcessaoAoBuscarUsuarioPorIdENaoEncontrar() {
        Long id = 99L;

        when(repository.findByIdAndExcluidoFalse(id)).thenReturn(Optional.empty());

        UsuarioNaoEncontradoException exception = assertThrows(UsuarioNaoEncontradoException.class,
                () -> service.getUsuario(id));

        assertEquals("Não foi encontrado nenhum usuário" +
                " com ID: '" + id + "'.", exception.getMessage());
    }
}
