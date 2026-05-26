package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
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

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerComentariosTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AcompanhamentoDeLeituraService acompanhamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /usuarios/{id}/comentarios deve retornar 200 com lista")
    void getComentariosPorUsuario() throws Exception {
        AcompanhamentoResponseDto dto = new AcompanhamentoResponseDto(2L, 5, 6, "Coment Usu", 7L, 8L);

        when(acompanhamentoService.listarPorUsuario(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/usuarios/1/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].comentario").value("Coment Usu"));
    }
}
