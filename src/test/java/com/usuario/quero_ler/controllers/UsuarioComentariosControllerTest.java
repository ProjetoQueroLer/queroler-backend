package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;
import com.usuario.quero_ler.repository.UserRepository;
import com.usuario.quero_ler.security.TokenService;
import com.usuario.quero_ler.exceptions.especies.UsuarioNaoEncontradoException;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
import com.usuario.quero_ler.service.UsuarioService;

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
class UsuarioComentariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AcompanhamentoDeLeituraService acompanhamentoService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

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

    @Test
    @DisplayName("GET /usuarios/{id}/comentarios deve retornar 200 com lista vazia")
    void getComentariosPorUsuario_emptyList() throws Exception {
        when(acompanhamentoService.listarPorUsuario(2L)).thenReturn(List.of());

        mockMvc.perform(get("/usuarios/2/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /usuarios/{id}/comentarios deve retornar múltiplos comentários")
    void getComentariosPorUsuario_multiple() throws Exception {
        AcompanhamentoResponseDto dto1 = new AcompanhamentoResponseDto(3L, 1, 2, "Primeiro", 4L, 5L);
        AcompanhamentoResponseDto dto2 = new AcompanhamentoResponseDto(4L, 3, 4, "Segundo", 4L, 5L);

        when(acompanhamentoService.listarPorUsuario(3L)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/usuarios/3/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].comentario").value("Segundo"));
    }

    @Test
    @DisplayName("GET /usuarios/{id}/comentarios quando usuário não existe retorna 404")
    void getComentariosPorUsuario_usuarioNaoEncontrado() throws Exception {
        when(acompanhamentoService.listarPorUsuario(99L))
                .thenThrow(new UsuarioNaoEncontradoException("Usuario não encontrado"));

        mockMvc.perform(get("/usuarios/99/comentarios").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}