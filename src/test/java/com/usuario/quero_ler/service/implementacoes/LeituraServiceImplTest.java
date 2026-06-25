package com.usuario.quero_ler.service.implementacoes;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.usuario.quero_ler.enums.LeituraStatus;
import com.usuario.quero_ler.exceptions.especies.LeituraEstadoInvalidoException;
import com.usuario.quero_ler.models.Leitura;

import static com.usuario.quero_ler.enums.LeituraStatus.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class LeituraServiceImplTest {

	@InjectMocks
	LeituraServiceImpl service;

	@ParameterizedTest
	@MethodSource("transicoesValidas")
	@DisplayName("Deve transicionar estados quando transições forem validas")
	void deveTransicionarEstadosValidosComSucesso(LeituraStatus estadoAtual, LeituraStatus estadoDestino) {
		Leitura leitura = new Leitura();
		leitura.setStatus(estadoAtual);

		service.ControleStatusLeitura(leitura, estadoDestino);

		assertEquals(leitura.getStatus(), estadoDestino);
	}

	@ParameterizedTest
	@MethodSource("transicoesInvalidas")
	@DisplayName("Deve lancar exceção, quando as transições forem invalidas")
	void deveLancarExcecaoQuandoTransicaoInvalida(LeituraStatus estadoAtual, LeituraStatus estadoDestino) {
		Leitura leitura = new Leitura();
		leitura.setStatus(estadoAtual);

		assertThrows(LeituraEstadoInvalidoException.class,
				() -> service.ControleStatusLeitura(leitura, estadoDestino), 
				String.format("Deveria lançar uma exceção para uma transição de estado inválida. Estado atual=%s, Estado destino=%s",estadoAtual,estadoDestino));

		assertNotEquals(
				String.format("Valores deveriam ser diferentes: Estado atual=%s, Estado destino=%s, Estado setado=%s",estadoAtual,estadoDestino,leitura.getStatus()),
				leitura.getStatus(), estadoDestino);
	}

	static Stream<Arguments> transicoesValidas() {
		return Stream.of(
				// fora da lista -> Quero Ler, Lendo, Lido
				arguments(null, LIVROS_QUE_QUERO_LER),
				arguments(null, LIVROS_QUE_ESTOU_LENDO),
				arguments(null, LIVROS_LIDOS),
				// Quero Ler -> Lendo, Lido, Abandonei
				arguments(LIVROS_QUE_QUERO_LER, LIVROS_QUE_ESTOU_LENDO),
				arguments(LIVROS_QUE_QUERO_LER, LIVROS_ABANDONADOS),
				arguments(LIVROS_QUE_QUERO_LER, LIVROS_LIDOS),
				// Estou Lendo -> Lido, Abandonei
				arguments(LIVROS_QUE_ESTOU_LENDO, LIVROS_ABANDONADOS),
				arguments(LIVROS_QUE_ESTOU_LENDO, LIVROS_LIDOS),
				// Lido -> Relendo
				arguments(LIVROS_LIDOS, RELENDO),
				// Relendo -> Lido, Abandonei
				arguments(RELENDO, LIVROS_LIDOS),
				arguments(RELENDO, LIVROS_ABANDONADOS),

				// Abandonei -> Quero ler, Lendo
				arguments(LIVROS_ABANDONADOS, LIVROS_QUE_QUERO_LER),
				arguments(LIVROS_ABANDONADOS, LIVROS_QUE_ESTOU_LENDO));
	}

	static Stream<Arguments> transicoesInvalidas() {
		return Stream.of(
				// null → abandonado, relendo (não permitido)
				arguments(null, LIVROS_ABANDONADOS),
				arguments(null, RELENDO),
				// QUERO_LER → quero_ler, relendo (não permitido)
				arguments(LIVROS_QUE_QUERO_LER, RELENDO),
				// LENDO → quero_ler, relendo (não permitido)
				arguments(LIVROS_QUE_ESTOU_LENDO, LIVROS_QUE_QUERO_LER),
				arguments(LIVROS_QUE_ESTOU_LENDO, RELENDO),
				// RELENDO → quero_ler, lendo (não permitido)
				arguments(RELENDO, LIVROS_QUE_QUERO_LER),
				arguments(RELENDO, LIVROS_QUE_ESTOU_LENDO),
				// ABANDONADO → quero_ler, lido, relendo (não permitido)
				arguments(LIVROS_ABANDONADOS, LIVROS_LIDOS),
				arguments(LIVROS_ABANDONADOS, RELENDO),
				// LIDO → quero_ler, lendo, abandonado (não permitido)
				arguments(LIVROS_LIDOS, LIVROS_QUE_QUERO_LER),
				arguments(LIVROS_LIDOS, LIVROS_QUE_ESTOU_LENDO),
				arguments(LIVROS_LIDOS, LIVROS_ABANDONADOS));
	}
}
