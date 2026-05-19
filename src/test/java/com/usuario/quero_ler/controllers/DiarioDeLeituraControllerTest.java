package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

                mockMvc.perform(post("/leituras")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isNotFound());
        }
}
