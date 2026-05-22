package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;
import com.usuario.quero_ler.dtos.livro.LivroResumoResponseDto;
import com.usuario.quero_ler.exceptions.especies.UsuarioLivroNaoEncontradoException;
import com.usuario.quero_ler.mappers.DiarioLeituraMapper;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.models.UsuarioLivroId;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import com.usuario.quero_ler.exceptions.especies.DadosDiarioInvalidoException;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiarioDeLeituraServiceImplTest {

	@InjectMocks
	private DiarioDeLeituraServiceImpl service;

	@Mock
	private DiarioDeLeituraRepository repository;

	@Mock
	private UsuarioLivroRepository usuarioLivroRepository;

	@Mock
	private LoginService loginService;

	@Mock
	private DiarioLeituraMapper diarioLeituraMapper;

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

		UsuarioLivroId id = new UsuarioLivroId();
		id.setUsuarioId(1L);
		id.setLivroId(2L);

		UsuarioLivro usuarioLivro = new UsuarioLivro();
		usuarioLivro.setId(id);
		usuarioLivro.setUsuario(new Usuario());

		User user = new User();
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		user.setUsuario(usuario);
		when(loginService.getUsuarioLogado()).thenReturn(user);

		when(usuarioLivroRepository.findByUsuarioIdAndLivroId(1L, 2L))
				.thenReturn(Optional.of(usuarioLivro));

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

		when(usuarioLivroRepository.findByUsuarioIdAndLivroId(1L, 2L))
				.thenReturn(Optional.empty());

		assertThrows(UsuarioLivroNaoEncontradoException.class, () -> service.criar(dto));

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
	@DisplayName("Deve buscar os dados de um diario com sucesso.")
	void deveBuscarOsDadosDeUmDiarioComSucesso() {
		Long livroId = 2L;
		Long usuarioId = 1L;

		UsuarioLivroId id = new UsuarioLivroId();
		id.setUsuarioId(usuarioId);
		id.setLivroId(livroId);

		Livro livro = new Livro();
		livro.setId(livroId);
		livro.setTitulo("Dom Casmurro");
		livro.setNumeroDePaginas(256);

		UsuarioLivro usuarioLivro = new UsuarioLivro();
		usuarioLivro.setId(id);
		usuarioLivro.setLivro(livro);

		DiarioDeLeitura diario = DiarioDeLeitura.builder()
				.id(10L)
				.usuarioLivro(usuarioLivro)
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
}
