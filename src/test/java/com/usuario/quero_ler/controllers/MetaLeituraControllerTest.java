package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("Deve deletar todas as metas de leitura do usuario")
    void deveDeltarMetasDoUsuario() throws Exception {

        doNothing().when(service).deletar();

        mockMvc.perform(delete("/metas"))
                .andExpect(status().isNoContent());

        verify(service).deletar();
    }
}