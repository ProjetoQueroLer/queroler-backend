package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;
import com.usuario.quero_ler.exceptions.especies.DiarioNaoEncontradoException;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;
import com.usuario.quero_ler.exceptions.especies.UsuarioLivroNaoEncontradoException;
import com.usuario.quero_ler.fixtures.DiarioLeituraFixtures;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.DiarioDeLeituraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.usuario.quero_ler.exceptions.especies.UsuarioSemPermissaoParaAcaoException;

@WebMvcTest(DiarioDeLeituraController.class)
@AutoConfigureMockMvc(addFilters = false)
class DiarioDeLeituraControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DiarioDeLeituraService service;

	@MockitoBean
	private TokenService tokenService;

	@MockitoBean
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("POST /leituras deve retornar 201 quando criar com sucesso")
	void postCriarSucesso() throws Exception {
		DiarioDeLeituraRequestDto requestDto = DiarioLeituraFixtures.novoDiarioDeLeitura();

		String json = objectMapper.writeValueAsString(requestDto);

		doNothing().when(service).criar(any(DiarioDeLeituraRequestDto.class));

		mockMvc.perform(post("/leituras")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("POST /leituras deve retornar 404 quando usuarioLivro não existir")
	void postCriarNotFound() throws Exception {
		DiarioDeLeituraRequestDto requestDto = DiarioLeituraFixtures.novoDiarioDeLeitura();

		String json = objectMapper.writeValueAsString(requestDto);

		doThrow(new UsuarioLivroNaoEncontradoException("Não encontrado"))
				.when(service).criar(any(DiarioDeLeituraRequestDto.class));

<<<<<<< HEAD
		mockMvc.perform(post("/leituras")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("GET /leituras deve retornar 200 e o json com os dados do Diario de leitura.")
	void deveRetornarODiarioDeLeituraComStatus200() throws Exception {

		DiarioDeLeituraResponseDto responseDto = DiarioLeituraFixtures.diarioDeLeituraResponse();

		when(service.buscarLeituraPorLivroEUsuario(2L)).thenReturn(responseDto);

		mockMvc.perform(get("/leituras")
				.param("livroId", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(responseDto.id()))
				.andExpect(jsonPath("$.livro.id").value(responseDto.livro().id()))
				.andExpect(jsonPath("$.livro.titulo").value(responseDto.livro().titulo()))
				.andExpect(jsonPath("$.livro.numeroDePaginas").value(responseDto.livro().numeroDePaginas()))
				.andExpect(jsonPath("$.tituloDaResenha").value(responseDto.tituloDaResenha()))
				.andExpect(jsonPath("$.resenha").value(responseDto.resenha()))
				.andExpect(jsonPath("$.spoilers").value(responseDto.spoilers()));

		verify(service).buscarLeituraPorLivroEUsuario(2L);
	}

	@Test
	@DisplayName("GET /leituras deve retornar 404.")
	void deveLancarExcecaoDiarioDeLeituraNaoEncontrado404() throws Exception {

		doThrow(new DiarioNaoEncontradoException("Diario não encontrado!"))
				.when(service).buscarLeituraPorLivroEUsuario(any(Long.class));
		mockMvc.perform(get("/leituras")
				.param("livroId", "99")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(service).buscarLeituraPorLivroEUsuario(99L);
	}

	@Test
	@DisplayName("PUT /leituras/{id} deve retornar 204 quando atualizar com sucesso")
	void putAtualizarSucesso() throws Exception {
		DiarioDeLeituraRequestDto requestDto = DiarioLeituraFixtures.novoDiarioDeLeitura();

		DiarioDeLeituraAtualizadoRequest updateDto = new DiarioDeLeituraAtualizadoRequest(
				requestDto.inicioDaLeitura(),
				requestDto.terminoDaLeitura(),
				requestDto.paginasLidas(),
				requestDto.nota(),
				requestDto.tituloDaResenha(),
				requestDto.resenha());

		String json = objectMapper.writeValueAsString(updateDto);

		doNothing().when(service).atualizar(any(Long.class), any(DiarioDeLeituraAtualizadoRequest.class));

		mockMvc.perform(put("/leituras/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("PUT /leituras/{id} deve retornar 404 quando diário não existir")
	void putAtualizarNotFound() throws Exception {
		DiarioDeLeituraRequestDto requestDto = DiarioLeituraFixtures.novoDiarioDeLeitura();

		DiarioDeLeituraAtualizadoRequest atualizadoDto = new DiarioDeLeituraAtualizadoRequest(
				requestDto.inicioDaLeitura(),
				requestDto.terminoDaLeitura(),
				requestDto.paginasLidas(),
				requestDto.nota(),
				requestDto.tituloDaResenha(),
				requestDto.resenha());

		String json = objectMapper.writeValueAsString(atualizadoDto);

		doThrow(new DiarioNaoEncontradoException("Não encontrado"))
				.when(service).atualizar(any(Long.class), any(DiarioDeLeituraAtualizadoRequest.class));

		mockMvc.perform(put("/leituras/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("PUT /leituras/{id} deve retornar 409 quando usuário não tiver permissão")
	void putAtualizarSemPermissao() throws Exception {
		DiarioDeLeituraRequestDto requestDto = DiarioLeituraFixtures.novoDiarioDeLeitura();

		DiarioDeLeituraAtualizadoRequest atualizadoDto = new DiarioDeLeituraAtualizadoRequest(
				requestDto.inicioDaLeitura(),
				requestDto.terminoDaLeitura(),
				requestDto.paginasLidas(),
				requestDto.nota(),
				requestDto.tituloDaResenha(),
				requestDto.resenha());

		String json = objectMapper.writeValueAsString(atualizadoDto);

		doThrow(new UsuarioSemPermissaoParaAcaoException("Sem permissão"))
				.when(service).atualizar(any(Long.class), any(DiarioDeLeituraAtualizadoRequest.class));

		mockMvc.perform(put("/leituras/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isConflict());
	}
}
