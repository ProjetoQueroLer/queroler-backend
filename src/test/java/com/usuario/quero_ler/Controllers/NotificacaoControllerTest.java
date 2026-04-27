package com.usuario.quero_ler.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.fixtures.NotificacaoFixture;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.service.NotificacaoServiceI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacaoController.class)
class NotificacaoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacaoServiceI service;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar notificacao com sucesso")
    void deveCriarNotificacao() throws Exception {
        NotificacaoRequestDto request = NotificacaoFixture.requestDto();
        NotificacaoResponseDto response = NotificacaoFixture.response();

        when(service.criar(any())).thenReturn(response);

        mockMvc.perform(post("/notificacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(service).criar(request);
    }

    @Test
    @DisplayName("Deve retornar notificações não lidas do usuário")
    void deveRetornarNotificacoesNaoLidasDoUsuario() throws Exception {

        Long idUsuario = 1L;

        NotificacaoResponseDto dto = NotificacaoFixture.response();
        Pageable pageable = PageRequest.of(0, 20);

        Page<NotificacaoResponseDto> page = new PageImpl<>(List.of(dto));

        when(service.naoLidas(idUsuario, pageable)).thenReturn(page);

        mockMvc.perform(get("/notificacoes/{id}/usuario", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).naoLidas(idUsuario, pageable);
    }

    @Test
    @DisplayName("Deve marcar as notificações do usuario como lida")
    void deveAlterarDocumento() throws Exception {
        Long idUsuario = 1L;

        mockMvc.perform(put("/notificacoes/{id}", idUsuario)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service).marcarComoLidas(idUsuario);
    }
}