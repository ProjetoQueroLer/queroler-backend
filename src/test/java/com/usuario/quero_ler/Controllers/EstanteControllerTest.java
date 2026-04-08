package com.usuario.quero_ler.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enuns.LivroStatus;
import com.usuario.quero_ler.fixtures.LivroFixture;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.EstanteDoUsuarioServiceI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EstanteController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstanteControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private EstanteDoUsuarioServiceI serviceI;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve adicionar um livro na estante do usuario.")
    void deveAdicionarUmLivroNaEstanteDoUsuario() throws Exception {
        Long idUsuario = 1L;
        Long idLivro = 10L;

        mockMvc.perform(post("/estante")
                        .param("idUsuario", idUsuario.toString())
                        .param("idLivro", idLivro.toString()))
                .andExpect(status().isCreated());

        verify(serviceI).adicionar(idUsuario, idLivro);
    }

    @Test
    @DisplayName("Deve retornar lista paginada de livros que quero ler")
    void deveRetornarLivrosQueQueroLer() throws Exception {
        Long idUsuario = 1L;

        LivroTelaLeituraResponse response = LivroFixture.responseTelaDeLeitura(LivroStatus.LIVROS_QUE_QUERO_LER);

        Page<LivroTelaLeituraResponse> page = new PageImpl<>(List.of(response));

        when(serviceI.lista(eq(idUsuario), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/estante/{id}", idUsuario)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "titulo,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(serviceI).lista(eq(idUsuario), any(Pageable.class));
    }

    @Test
    @DisplayName("Deve mudar o status do livro na estante")
    void deveMudarStatusDoLivro() throws Exception {

        Long idUsuario = 1L;
        String isbn = "123456789";
        LivroStatus status = LivroStatus.LIVROS_ABANDONADOS;

        mockMvc.perform(put("/estante/{id}/status", idUsuario)
                        .param("isbn", isbn)
                        .param("status", status.name()))
                .andExpect(status().isOk());

        verify(serviceI).mudarStatus(idUsuario, isbn, status);
    }
}