package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
@AutoConfigureMockMvc(addFilters = false)
class LivroControllerComentariosTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AcompanhamentoDeLeituraService acompanhamentoService;

    @MockitoBean
    private LivroService livroService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("GET /livros/{id}/comentarios deve retornar 200 com lista")
    void getComentariosPorLivro() throws Exception {
        AcompanhamentoResponseDto dto = new AcompanhamentoResponseDto(1L, 1, 2, "Coment", 3L, 4L);

        when(acompanhamentoService.listarPorLivro(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/livros/1/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].comentario").value("Coment"));
    }

    @Test
    @DisplayName("GET /livros/{id}/comentarios deve retornar 200 com lista vazia")
    void getComentariosPorLivroVazio() throws Exception {
        when(acompanhamentoService.listarPorLivro(2L)).thenReturn(List.of());

        mockMvc.perform(get("/livros/2/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /livros/{id}/comentarios deve retornar 200 com vários comentários")
    void getComentariosPorLivroMultiplo() throws Exception {
        AcompanhamentoResponseDto dto1 = new AcompanhamentoResponseDto(1L, 1, 2, "ComentA", 3L, 4L);
        AcompanhamentoResponseDto dto2 = new AcompanhamentoResponseDto(2L, 5, 6, "ComentB", 7L, 8L);

        when(acompanhamentoService.listarPorLivro(10L)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/livros/10/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].comentario").value("ComentB"));
    }

    @Test
    @DisplayName("GET /livros/{id}/comentarios deve retornar 500 quando serviço falhar")
    void getComentariosPorLivroServiceError() throws Exception {
        when(acompanhamentoService.listarPorLivro(3L)).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/livros/3/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
