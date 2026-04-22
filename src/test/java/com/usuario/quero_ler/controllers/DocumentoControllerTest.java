package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.fixtures.DocumentoFixture;
import com.usuario.quero_ler.service.DocumentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(DocumentoController.class)
class DocumentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DocumentoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar documento com sucesso")
    void deveCriarDocumento() throws Exception {
        DocumentoRequestDto request = DocumentoFixture.requestDto();
        DocumentoResponseDto response = DocumentoFixture.responseDto();

        when(service.criar(any())).thenReturn(response);

        mockMvc.perform(post("/documentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(service).criar(request);
    }

    @Test
    @DisplayName("Deve retornar termos gerais de uso")
    void deveRetornarTermosGerais() throws Exception {

        DocumentoResponseDto response = DocumentoFixture.responseDto();

        when(service.getTermosGeraisDeUso()).thenReturn(response);

        mockMvc.perform(get("/documentos/termos-gerais-de-uso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.titulo").value(response.titulo()))
                .andExpect(jsonPath("$.conteudo").value(response.conteudo()));

        verify(service).getTermosGeraisDeUso();
    }

    @Test
    @DisplayName("Deve alterar documento com sucesso")
    void deveAlterarDocumento() throws Exception {

        DocumentoAlteracoesDto dto = new DocumentoAlteracoesDto(
                "Titulo novo",null,"Conteudo novo"
        );

        mockMvc.perform(put("/documentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        verify(service).alterar(eq(1L), any());
    }
}