package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.dtos.usuario.UsuarioRequestDto;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.CredenciaisInvalidasException;
import com.usuario.quero_ler.exceptions.especies.UsuarioComPerfilInvalidoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoEncontradoException;
import com.usuario.quero_ler.fixtures.LoginFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl service;

    @Mock
    private UserRepository repository;

		@Autowired
			HttpServletResponse response;

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
        String senha = "Teste123&";
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        LoginRequestDto dto = LoginFixture.requestDto();

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.of(user));

        service.login(dto, response);

    }

    @Test
    @DisplayName("Deve lançar excessão ao tentar login com usuário não cadastrado.")
    void deveLancarExcessaoAoFazerLoginComUsuarioNaoCadastrado() {
        LoginRequestDto dto = LoginFixture.requestDto();

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.empty());

        UsuarioNaoEncontradoException exception = assertThrows(UsuarioNaoEncontradoException.class,
                () -> service.login(dto, response)
        );

        assertEquals("Usuario não cadastrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar excessão ao tentar login com profile inválido.")
    void deveLancarExcessaoAoFazerLoginComProfileInvalido() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        LoginRequestDto dto = LoginFixture.requestDto();

        when(repository.findByUserIgnoreCase(dto.user())).thenReturn(Optional.of(user));

        UsuarioComPerfilInvalidoException exception = assertThrows(UsuarioComPerfilInvalidoException.class,
                () -> service.login(dto, response)
        );

        assertEquals("Perfil inválido", exception.getMessage());
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

        assertEquals("Senha inválida.", exception.getMessage());
    }
}
