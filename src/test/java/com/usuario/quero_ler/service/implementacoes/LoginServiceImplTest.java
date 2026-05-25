package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.dtos.login.LoginResponseDto;
import com.usuario.quero_ler.dtos.usuario.UsuarioRequestDto;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.CredenciaisInvalidasException;
import com.usuario.quero_ler.exceptions.especies.SenhaInvalidaException;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoEncontradoException;
import com.usuario.quero_ler.fixtures.LoginFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("Deve criar um login com sucesso")
    void deveCriarUmLoginComSucesso() {
        UsuarioRequestDto dto = UserFixture.requestDto();
        UsuarioProfile profile = UsuarioProfile.LEITOR;
        User user = UserFixture.userEntity(profile);

        when(repository.save(any(User.class))).thenReturn(user);

        User resposta = service.criar(dto, profile);

        assertNotNull(resposta.getId());
        assertEquals(dto.email(), resposta.getUser());
        assertEquals(resposta.getProfile(), profile);
    }

    @Test
    @DisplayName("Deve validar um login com sucesso;")
    void deveFazerLoginComSucesso() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        LoginRequestDto dto = LoginFixture.requestDto();
        String token = "token.mock";

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(user)).thenReturn(token);

        LoginResponseDto retorno = service.login(dto, response);

        verify(tokenService).generateToken(user);
    }

    @Test
    @DisplayName("Deve validar retornar primeiro login para administrador ou moderador")
    void deveRerornarPrimeiroLogin() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        LoginRequestDto dto = LoginFixture.requestDto();
        String token = "token.mock";

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(user)).thenReturn(token);

        LoginResponseDto retorno = service.login(dto, response);

        assertEquals(true,retorno.primeiroLogin());

        verify(tokenService).generateToken(user);
    }

    @Test
    @DisplayName("Deve lançar excessão ao tentar login com usuário não cadastrado.")
    void deveLancarExcessaoAoFazerLoginComUsuarioNaoCadastrado() {
        LoginRequestDto dto = LoginFixture.requestDto();

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.empty());

        UsuarioNaoEncontradoException exception = assertThrows(UsuarioNaoEncontradoException.class,
                () -> service.login(dto, response)
        );

        assertEquals("Usuario não cadastrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve permitir login para perfil administrador.")
    void devePermitirLoginComPerfilAdministrador() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        LoginRequestDto dto = LoginFixture.requestDto();
        String token = "token.admin.mock";

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(user)).thenReturn(token);

        assertDoesNotThrow(() -> service.login(dto, response));
        verify(tokenService).generateToken(user);
    }

    @Test
    @DisplayName("Deve lançar excessão ao tentar login com senha inválida.")
    void deveLancarExcessaoAoFazerLoginComSenhaInvalido() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        LoginRequestDto dto = new LoginRequestDto(user.getUser(), "Teste1234$");

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.of(user));

        CredenciaisInvalidasException exception = assertThrows(CredenciaisInvalidasException.class,
                () -> service.login(dto, response)
        );

        assertEquals("Senha incorreta.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar login com senha vazia.")
    void deveLancarExcecaoAoTentarCriarLoginComSenhaSemLetrasMaiusculas() {
        String senha = "";
        UsuarioRequestDto dto = UserFixture.requestDto(senha);
        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> service.criar(dto, UsuarioProfile.LEITOR));
        assertEquals("Senha é obrigatória.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar login com senha com menos de 08 caracteres.")
    void deveLancarExcecaoAoTentarCriarLoginComSenhaMenorQue08Caracteres() {
        String senha = "Teste1$";
        UsuarioRequestDto dto = UserFixture.requestDto(senha);
        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> service.criar(dto, UsuarioProfile.LEITOR));
        assertEquals("A senha deve ter no mínimo 8 caracteres.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar login com senha sem letras maiusculas.")
    void deveLancarExcecaoAoTentarCriarLoginSemLetrasMaiusculas() {
        String senha = "teste123$";
        UsuarioRequestDto dto = UserFixture.requestDto(senha);
        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> service.criar(dto, UsuarioProfile.LEITOR));
        assertEquals("A senha deve conter pelo menos uma letra maiúscula.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar login com senha sem letras minúsculas.")
    void deveLancarExcecaoAoTentarCriarLoginSemLetrasMinusculas() {
        String senha = "TESTES123$";
        UsuarioRequestDto dto = UserFixture.requestDto(senha);
        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> service.criar(dto, UsuarioProfile.LEITOR));
        assertEquals("A senha deve conter pelo menos uma letra minúscula.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar login com senha sem números.")
    void deveLancarExcecaoAoTentarCriarLoginSemNumero() {
        String senha = "TestesSemNumeros$";
        UsuarioRequestDto dto = UserFixture.requestDto(senha);
        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> service.criar(dto, UsuarioProfile.LEITOR));
        assertEquals("A senha deve conter pelo menos um número.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar login com senha sem caracteres especiais.")
    void deveLancarExcecaoAoTentarCriarLoginSemCaracteresEspeciais() {
        String senha = "Testes12345";
        UsuarioRequestDto dto = UserFixture.requestDto(senha);
        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> service.criar(dto, UsuarioProfile.LEITOR));
        assertEquals("A senha deve conter pelo menos um caractere especial.", exception.getMessage());
    }
}