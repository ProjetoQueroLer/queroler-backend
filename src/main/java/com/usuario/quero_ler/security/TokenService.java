package com.usuario.quero_ler.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.usuario.quero_ler.exceptions.especies.GerarTokenException;
import com.usuario.quero_ler.models.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

	@Value("${api.security.token.secret}")
	private String secret;

	@Value("${api.security.token.issuer:quero_ler}")
	private String issuer;

	@Value("${api.security.token.expiration-minutes:120}")
	private long expirationMinutes;

	@PostConstruct
	void validateConfig() {
		if (secret == null || secret.isBlank() || secret.length() < 32) {
			throw new IllegalStateException("api.security.token.secret deve ter ao menos 32 caracteres.");
		}
	}

	public String generateToken(User user) {
		try {
			Instant now = Instant.now();
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.create()
					.withIssuer(issuer)
					.withSubject(user.getUser())
					.withClaim("role", user.getProfile().name())
					.withIssuedAt(now)
					.withJWTId(java.util.UUID.randomUUID().toString())
					.withExpiresAt(genExpirationDate(now))
					.sign(algorithm);
		} catch (JWTCreationException exception) {
			throw new GerarTokenException("Erro ao gerar token", exception);
		}
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer(issuer)
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException exception) {
			return null;
		}
	}

	private Instant genExpirationDate(Instant now) {
		return now.plus(Duration.ofMinutes(expirationMinutes));
	}
}
