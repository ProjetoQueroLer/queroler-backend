package com.usuario.quero_ler.service.implementacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.usuario.quero_ler.exceptions.especies.*;
import com.usuario.quero_ler.fixtures.LivroFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;
import com.usuario.quero_ler.dtos.leitura.LivroAcompanhamentoResponseDto;
import com.usuario.quero_ler.dtos.livro.LivroResumoResponseDto;
import com.usuario.quero_ler.mappers.DiarioLeituraMapper;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.models.Leitura;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.repository.LeituraRepository;
import com.usuario.quero_ler.service.LeituraService;
import com.usuario.quero_ler.service.LoginService;

@ExtendWith(MockitoExtension.class)
class DiarioDeLeituraServiceImplTest {

	@InjectMocks
	private DiarioDeLeituraServiceImpl service;

	@Mock
	private DiarioDeLeituraRepository repository;

	@Mock
	private LeituraRepository leituraRepository;

	@Mock
	private LoginService loginService;

	@Mock
	private DiarioLeituraMapper diarioLeituraMapper;

	@Mock
	private LeituraService leituraService;

	@Test
	@DisplayName("Deve lançar exceção ao tentar criar diario de leitura sem numero de paginas")
	void deveLancarExcecaoAoTentarCriarDiarioSemNumeroDePaginas() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				LocalDateTime.now(),
				600,
				5.0,
				"Título",
				"resenha",
				true);


		Leitura leitura = new Leitura();
		leitura.setId(1L);
		leitura.setUsuario(new Usuario());

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);

		Livro livro = LivroFixture.entity();
		leitura.setLivro(livro);

		when(loginService.getUsuarioLogado()).thenReturn(user);

		when(leituraRepository.findByUsuarioIdAndLivroId(1L, 2L))
				.thenReturn(Optional.of(leitura));

		NumeroDePaginasInvalidaException exception = assertThrows(NumeroDePaginasInvalidaException.class,
				()-> service.criar(dto));

		assertEquals("O número de páginas lidas não pode ser maior que o total de páginas do livro." +
						" Total: ("+livro.getNumeroDePaginas()+")",
				exception.getMessage());
	}

	@Test
	@DisplayName("Deve salvar diario de leitura quando usuarioLivro existir")
	void deveSalvarDiarioQuandoUsuarioLivroExistir() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				LocalDateTime.now(),
				10,
				5.0,
				"Título",
				"resenha",
				true);


		Leitura leitura = new Leitura();
		leitura.setId(1L);
		leitura.setUsuario(new Usuario());

		Livro livro = LivroFixture.entity();
		leitura.setLivro(livro);

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);

		when(loginService.getUsuarioLogado()).thenReturn(user);

		when(leituraRepository.findByUsuarioIdAndLivroId(1L, 2L))
				.thenReturn(Optional.of(leitura));

		service.criar(dto);

		verify(repository).save(any());
	}

	@Test
	@DisplayName("Deve lançar UsuarioLivroNaoEncontradoException quando não existir")
	void deveLancarQuandoNaoExistir() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				LocalDateTime.now(),
				10,
				5.0,
				"Título",
				"resenha",
				true);

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);
		when(loginService.getUsuarioLogado()).thenReturn(user);

		when(leituraRepository.findByUsuarioIdAndLivroId(1L, 2L))
				.thenReturn(Optional.empty());

		assertThrows(LeituraNaoEncontradaException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando inicio da leitura for no futuro")
	void deveLancarQuandoInicioNoFuturo() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().plusDays(1),
				null,
				10,
				4.0,
				"Título",
				"resenha",
				true);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando termino for anterior ao inicio")
	void deveLancarQuandoTerminoAntesDoInicio() {
		LocalDateTime inicio = LocalDateTime.now();
		LocalDateTime termino = inicio.minusDays(1);

		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				inicio,
				termino,
				10,
				4.0,
				"Título",
				"resenha",
				true);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando paginasLidas for negativa")
	void deveLancarQuandoPaginasNegativas() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				null,
				-5,
				4.0,
				"Título",
				"resenha",
				true);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando nota estiver fora do intervalo")
	void deveLancarQuandoNotaForaIntervalo() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				null,
				10,
				6.0,
				"Título",
				"resenha",
				true);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando nota tiver mais de uma casa decimal")
	void deveLancarQuandoNotaTiverMaisDeUmaCasaDecimal() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				null,
				10,
				4.555,
				"Título",
				"resenha",
				true);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando nota nao for multiplo de 0.5")
	void deveLancarQuandoNotaNaoForMultiploDeMeio() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				null,
				10,
				4.6,
				"Título",
				"resenha",
				true);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando nota for zero")
	void deveLancarQuandoNotaForZero() {
		DiarioDeLeituraRequestDto dto = new DiarioDeLeituraRequestDto(
				2L,
				LocalDateTime.now().minusDays(1),
				null,
				10,
				0.0,
				"Título",
				"resenha",
				true);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.criar(dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve buscar os dados de um diario com sucesso.")
	void deveBuscarOsDadosDeUmDiarioComSucesso() {
		Long livroId = 2L;
		Long usuarioId = 1L;


		Livro livro = new Livro();
		livro.setId(livroId);
		livro.setTitulo("Dom Casmurro");
		livro.setNumeroDePaginas(256);

		Leitura leitura = new Leitura();
		leitura.setId(1L);
		leitura.setLivro(livro);

		DiarioDeLeitura diario = DiarioDeLeitura.builder()
				.id(10L)
				.leitura(leitura)
				.inicioDaLeitura(LocalDateTime.now().minusDays(5))
				.terminoDaLeitura(null)
				.paginasLidas(50)
				.nota(4.5)
				.tituloDaResenha("Ótima leitura")
				.resenha("Livro muito bom...")
				.spoiler(false)
				.build();

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		user.setUsuario(usuario);

		when(loginService.getUsuarioLogado()).thenReturn(user);
		LivroResumoResponseDto livroResumo = new LivroResumoResponseDto(livroId, "Dom Casmurro", 256);
		DiarioDeLeituraResponseDto diarioResponse = new DiarioDeLeituraResponseDto(
				10L,
				livroResumo,
				diario.getInicioDaLeitura(),
				diario.getTerminoDaLeitura(),
				List.of(),
				4.5,
				"Ótima leitura",
				"Livro muito bom...",
				false);
		when(diarioLeituraMapper.toResponse(diario))
				.thenReturn(diarioResponse);
		when(repository.findByUsuarioIdAndLivroId(usuarioId, livroId))
				.thenReturn(Optional.of(diario));

		DiarioDeLeituraResponseDto resultado = service.buscarLeituraPorLivroEUsuario(livroId);

		assertNotNull(resultado);
		assertEquals(10L, resultado.id());
		assertEquals(livroId, resultado.livro().id());
		assertEquals("Dom Casmurro", resultado.livro().titulo());
		assertEquals(256, resultado.livro().numeroDePaginas());
		assertEquals(4.5, resultado.nota());
		assertEquals("Ótima leitura", resultado.tituloDaResenha());
		assertEquals("Livro muito bom...", resultado.resenha());
		assertFalse(resultado.spoilers());

		verify(loginService).getUsuarioLogado();
		verify(repository).findByUsuarioIdAndLivroId(usuarioId, livroId);
	}

	@Test
	@DisplayName("Deve gerar uma exception ao tentar buscar o diario.")
	void deveLancarUmaExcecaoAoBuscarDadosDeUmDiario() {
		Long livroId = 2L;
		Long usuarioId = 1L;

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		user.setUsuario(usuario);

		when(loginService.getUsuarioLogado()).thenReturn(user);
		assertThrows(DiarioNaoEncontradoException.class, () -> service.buscarLeituraPorLivroEUsuario(livroId));

		verify(loginService).getUsuarioLogado();
		verify(repository).findByUsuarioIdAndLivroId(usuarioId, livroId);
	}

	@Test
	@DisplayName("Deve atualizar diario quando existir e pertencer ao usuario")
	void deveAtualizarQuandoExistirEProprio() {
		DiarioDeLeituraAtualizadoRequest dto = new DiarioDeLeituraAtualizadoRequest(
				LocalDateTime.now().minusDays(2),
				LocalDateTime.now().minusDays(1),
				20,
				4.0,
				"Título atualizado",
				"resenha atualizada");

		DiarioDeLeitura diario = new DiarioDeLeitura();

		Leitura leitura = new Leitura();
		leitura.setId(1L);
		Usuario dono = new Usuario();
		dono.setId(1L);
		leitura.setUsuario(dono);

		diario.setLeitura(leitura);

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);
		when(loginService.getUsuarioLogado()).thenReturn(user);

		when(repository.findById(1L)).thenReturn(Optional.of(diario));

		service.atualizar(1L, dto);

		verify(repository).save(any(DiarioDeLeitura.class));
	}

	@Test
	@DisplayName("Deve lançar DiarioNaoEncontradoException quando não existir")
	void deveLancarQuandoDiarioNaoExistir() {
		DiarioDeLeituraAtualizadoRequest dto = new DiarioDeLeituraAtualizadoRequest(
				LocalDateTime.now().minusDays(2),
				LocalDateTime.now().minusDays(1),
				20,
				4.0,
				"Título",
				"resenha");

		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(DiarioNaoEncontradoException.class, () -> service.atualizar(1L, dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar UsuarioSemPermissaoParaAcaoException quando diário não pertencer ao usuário")
	void deveLancarQuandoNaoForDono() {
		DiarioDeLeituraAtualizadoRequest dto = new DiarioDeLeituraAtualizadoRequest(
				LocalDateTime.now().minusDays(2),
				LocalDateTime.now().minusDays(1),
				20,
				4.0,
				"Título",
				"resenha");

		DiarioDeLeitura diario = new DiarioDeLeitura();

		Leitura leitura = new Leitura();
		leitura.setId(1L);
		Usuario dono = new Usuario();
		dono.setId(2L);
		leitura.setUsuario(dono);

		diario.setLeitura(leitura);

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);
		when(loginService.getUsuarioLogado()).thenReturn(user);

		when(repository.findById(1L)).thenReturn(Optional.of(diario));

		assertThrows(UsuarioSemPermissaoParaAcaoException.class, () -> service.atualizar(1L, dto));

		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException quando payload for inválido")

	void deveLancarQuandoPayloadInvalido() {
		DiarioDeLeituraAtualizadoRequest dto = new DiarioDeLeituraAtualizadoRequest(
				LocalDateTime.now().plusDays(1),
				null,
				0,
				3.0,
				"Título",
				"resenha");

		assertThrows(DadosDiarioInvalidoException.class, () -> service.atualizar(1L, dto));

		verify(repository, never()).findById(any());
		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DiarioNaoEncontradoException ao excluir um diário inexistente")
	void deveLancarQuandoDiarioNaoExistirAoExcluir() {
		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(DiarioNaoEncontradoException.class, () -> service.excluirDiarioDeLeitura(1L));

		verify(leituraRepository, never()).save(any());
		verify(repository, never()).delete(any());
	}

	@Test
	@DisplayName("Deve voltar a leitura para relendo ao excluir um diário de um livro já lido")
	void deveVoltarParaRelendoAoExcluirDiarioDeLivroLido() {
		DiarioDeLeitura diario = new DiarioDeLeitura();
		Leitura leitura = new Leitura();
		leitura.setId(1L);
		leitura.setStatus(com.usuario.quero_ler.enums.LeituraStatus.LIVROS_LIDOS);
		leitura.setLido(true);
		Usuario dono = new Usuario();
		dono.setId(1L);
		leitura.setUsuario(dono);
		diario.setLeitura(leitura);

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);
		when(loginService.getUsuarioLogado()).thenReturn(user);
		when(repository.findById(1L)).thenReturn(Optional.of(diario));

		service.excluirDiarioDeLeitura(1L);

		verify(leituraService).ControleStatusLeitura(leitura, com.usuario.quero_ler.enums.LeituraStatus.RELENDO);
		verify(leituraRepository).save(leitura);
		verify(repository).delete(diario);
	}

	@Test
	@DisplayName("Deve reabrir a leitura para lendo ao excluir um diário de um livro abandonado")
	void deveReabrirParaLendoAoExcluirDiarioDeLivroAbandonado() {
		DiarioDeLeitura diario = new DiarioDeLeitura();
		Leitura leitura = new Leitura();
		leitura.setId(1L);
		leitura.setStatus(com.usuario.quero_ler.enums.LeituraStatus.LIVROS_ABANDONADOS);
		leitura.setLido(false);
		Usuario dono = new Usuario();
		dono.setId(1L);
		leitura.setUsuario(dono);
		diario.setLeitura(leitura);

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);
		when(loginService.getUsuarioLogado()).thenReturn(user);
		when(repository.findById(1L)).thenReturn(Optional.of(diario));

		service.excluirDiarioDeLeitura(1L);

		verify(leituraService).ControleStatusLeitura(leitura, com.usuario.quero_ler.enums.LeituraStatus.LIVROS_QUE_ESTOU_LENDO);
		verify(leituraRepository).save(leitura);
		verify(repository).delete(diario);
  }
  
  @Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException ao atualizar quando nota tiver mais de uma casa decimal")
	void deveLancarAoAtualizarQuandoNotaTiverMaisDeUmaCasaDecimal() {
		DiarioDeLeituraAtualizadoRequest dto = new DiarioDeLeituraAtualizadoRequest(
				null, null, null, 4.555, null, null);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.atualizar(1L, dto));

		verify(repository, never()).findById(any());
		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException ao atualizar quando nota nao for multiplo de 0.5")
	void deveLancarAoAtualizarQuandoNotaNaoForMultiploDeMeio() {
		DiarioDeLeituraAtualizadoRequest dto = new DiarioDeLeituraAtualizadoRequest(
				null, null, null, 4.6, null, null);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.atualizar(1L, dto));

		verify(repository, never()).findById(any());
		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve lançar DadosDiarioInvalidoException ao atualizar quando nota for zero")
	void deveLancarAoAtualizarQuandoNotaForZero() {
		DiarioDeLeituraAtualizadoRequest dto = new DiarioDeLeituraAtualizadoRequest(
				null, null, null, 0.0, null, null);

		assertThrows(DadosDiarioInvalidoException.class, () -> service.atualizar(1L, dto));

		verify(repository, never()).findById(any());
		verify(repository, never()).save(any());
	}

	@Test
	@DisplayName("Deve listar leituras em andamento do usuário com sucesso.")
	void deveListarLeiturasEmAndamento() {
		Long usuarioId = 1L;

		Livro livro = new Livro();
		livro.setId(2L);
		livro.setTitulo("Dom Casmurro");

		Leitura leitura = new Leitura();
		leitura.setId(1L);
		leitura.setLivro(livro);

		DiarioDeLeitura diario = DiarioDeLeitura.builder()
				.id(10L)
				.leitura(leitura)
				.inicioDaLeitura(LocalDateTime.now().minusDays(3))
				.terminoDaLeitura(null)
				.build();

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		user.setUsuario(usuario);

		LivroAcompanhamentoResponseDto dto = new LivroAcompanhamentoResponseDto(
				10L, 2L, "Dom Casmurro", "/livros/2/capa", List.of(), diario.getInicioDaLeitura());

		when(loginService.getUsuarioLogado()).thenReturn(user);
		when(repository.findEmAndamentoPorUsuario(usuarioId)).thenReturn(List.of(diario));
		when(diarioLeituraMapper.toLivroAcompanhamentoResponse(diario)).thenReturn(dto);

		List<LivroAcompanhamentoResponseDto> resultado = service.listarEmAndamento();

		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals(10L, resultado.get(0).diarioId());
		assertEquals(2L, resultado.get(0).livroId());
		assertEquals("Dom Casmurro", resultado.get(0).titulo());

		verify(loginService).getUsuarioLogado();
		verify(repository).findEmAndamentoPorUsuario(usuarioId);
	}

	@Test
	@DisplayName("Deve retornar lista vazia quando o usuário não tiver leituras em andamento.")
	void deveRetornarListaVaziaQuandoNaoTiverLeiturasEmAndamento() {
		Long usuarioId = 1L;

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		user.setUsuario(usuario);

		when(loginService.getUsuarioLogado()).thenReturn(user);
		when(repository.findEmAndamentoPorUsuario(usuarioId)).thenReturn(List.of());

		List<LivroAcompanhamentoResponseDto> resultado = service.listarEmAndamento();

		assertNotNull(resultado);
		assertEquals(0, resultado.size());

		verify(repository).findEmAndamentoPorUsuario(usuarioId);
	}
}
