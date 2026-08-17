package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.leitura.AcompanhamentoRequestDto;
import com.usuario.quero_ler.exceptions.especies.DadosDiarioInvalidoException;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AcompanhamentoDeLeituraController.class)
@AutoConfigureMockMvc(addFilters = false)
class AcompanhamentoLeituraComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AcompanhamentoDeLeituraService service;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /leituras/{id}/comentarios deve retornar 201 quando sucesso")
    void postCriarComentarioSucesso() throws Exception {
        AcompanhamentoRequestDto dto = new AcompanhamentoRequestDto(1, 2, "coment");

        String json = objectMapper.writeValueAsString(dto);

        doNothing().when(service).adicionarComentario(any(Long.class), any(AcompanhamentoRequestDto.class));

        mockMvc.perform(post("/leituras/1/comentarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /leituras/{id}/comentarios deve retornar 400 quando payload invalido ou diario nao existir")
    void postCriarComentarioBadRequest() throws Exception {
        AcompanhamentoRequestDto dto = new AcompanhamentoRequestDto(1, 2, "coment");

        String json = objectMapper.writeValueAsString(dto);

        doThrow(new DadosDiarioInvalidoException("Erro")).when(service).adicionarComentario(any(Long.class),
                any(AcompanhamentoRequestDto.class));

        mockMvc.perform(post("/leituras/99/comentarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /leituras/{id}/comentarios deve retornar 400 quando pagina inicial for maior que pagina final")
    void postCriarComentarioPaginaInicialMaiorQueFinal() throws Exception {
        AcompanhamentoRequestDto dto = new AcompanhamentoRequestDto(50, 25, null);

        String json = objectMapper.writeValueAsString(dto);

        doThrow(new DadosDiarioInvalidoException("A página inicial deve ser menor que a página final."))
                .when(service).adicionarComentario(any(Long.class), any(AcompanhamentoRequestDto.class));

        mockMvc.perform(post("/leituras/1/comentarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isBadRequest());
    }
}
