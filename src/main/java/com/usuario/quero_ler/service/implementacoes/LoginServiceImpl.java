package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.dtos.usuario.UsuarioRequestDto;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.CredenciaisInvalidasException;
import com.usuario.quero_ler.exceptions.especies.UsuarioComPerfilInvalidoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoAutenticadoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoEncontradoException;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.utils.Senhas;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {
	private final UserRepository repository;

	private final TokenService tokenService;
	private final PasswordEncoder passwordEncoder;

	@Value("${api.security.token.expiration-minutes:120}")
	private long tokenExpirationMinutes;

	@Transactional
	@Override
	public User criar(UsuarioRequestDto dto, UsuarioProfile profile) {
		User user = new User();
		String senha = Senhas.gerar(dto.senha());
		user.setUser(dto.email());
		user.setSenha(passwordEncoder.encode(dto.senha()));
		user.setProfile(profile);
		user = repository.save(user);
		return user;
	}

	@Override
	public void login(LoginRequestDto dto, HttpServletResponse response) {
		Senhas.validar(dto.senha());
		User user = repository.findByUserIgnoreCase(dto.user())
				.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não cadastrado"));
		if (!passwordEncoder.matches(dto.senha(), user.getSenha())) {
			throw new CredenciaisInvalidasException("E-mail ou senha inválida.");
		}

		String token = tokenService.generateToken(user);

		ResponseCookie cookie = ResponseCookie.from("jwt", token)
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(Duration.ofMinutes(tokenExpirationMinutes))
				.sameSite("Lax")
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

	}

}
