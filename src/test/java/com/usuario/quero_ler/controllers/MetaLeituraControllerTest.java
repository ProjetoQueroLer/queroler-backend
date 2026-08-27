package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.dtos.meta.MetaResponseDto;
import com.usuario.quero_ler.fixtures.MetaLeituraFixture;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.MetaLeituraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MetaLeituraControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    MetaLeituraService service;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve adicionar uma meta de leitura com sucesso")
    void deveCriarMeta() throws Exception {
        Integer proximoAno = LocalDate.now().plusYears(1).getYear();
        MetaRequestDto dto = MetaLeituraFixture.requestDto(proximoAno);

        mockMvc.perform(post("/metas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service).novaMeta(dto);
    }

    @Test
    @DisplayName("Deve atualizar uma meta de leitura com sucesso")
    void deveAtualizarMeta() throws Exception {
        Integer ano = LocalDate.now().getYear();
        MetaRequestDto dto = MetaLeituraFixture.requestDto(ano);

        doNothing().when(service).atualizar(dto);

        mockMvc.perform(put("/metas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        verify(service).atualizar(dto);
    }

    @Test
    @DisplayName("Deve deletar todas as metas de leitura do usuario")
    void deveDeletarMetasDoUsuario() throws Exception {

        doNothing().when(service).deletar();

        mockMvc.perform(delete("/metas"))
                .andExpect(status().isNoContent());

        verify(service).deletar();
    }

    @Test
    @DisplayName("Deve retornar as metas de leitura do usuario do ano corrente")
    void deveRetornarAsMetasDoUsuarioDoAnoCorrente() throws Exception {
        MetaResponseDto response = MetaLeituraFixture.metaResponseDto();

        when(service.getMetas()).thenReturn(response);

        mockMvc.perform(get("/metas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ano").value(response.ano()))
                .andExpect(jsonPath("$.metaLivrosAno").value(response.metaLivrosAno()))
                .andExpect(jsonPath("$.metaLivrosMes").value(response.metaLivrosMes()))
                .andExpect(jsonPath("$.metaPaginasDia").value(response.metaPaginasDia()));

        verify(service).getMetas();
    }

    @Test
    @DisplayName("Deve retornar 400 quando metaLivrosAno for negativo")
    void deveRejeitarMetaLivrosAnoNegativo() throws Exception {
        MetaRequestDto dto = new MetaRequestDto(LocalDate.now().getYear(), -1, 1, 30);

        mockMvc.perform(post("/metas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(service, never()).novaMeta(any());
    }

    @Test
    @DisplayName("Deve retornar 400 quando metaLivrosMes for negativo")
    void deveRejeitarMetaLivrosMesNegativo() throws Exception {
        MetaRequestDto dto = new MetaRequestDto(LocalDate.now().getYear(), 12, -1, 30);

        mockMvc.perform(post("/metas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(service, never()).novaMeta(any());
    }

    @Test
    @DisplayName("Deve retornar 400 quando metaPaginasDia for negativo")
    void deveRejeitarMetaPaginasDiaNegativo() throws Exception {
        MetaRequestDto dto = new MetaRequestDto(LocalDate.now().getYear(), 12, 1, -1);

        mockMvc.perform(post("/metas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verify(service, never()).novaMeta(any());
    }
}