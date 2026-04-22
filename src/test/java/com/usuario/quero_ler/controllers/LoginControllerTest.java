package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.fixtures.LoginFixture;
import com.usuario.quero_ler.service.LoginService;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService service;

    @Autowired
    private ObjectMapper objectMapper;

		@Autowired
		HttpServletResponse response;
    @Test
    @DisplayName("Deve realizar um login com sucesso")
    void deveRealizarLoginComSucesso() throws Exception {
        LoginRequestDto request = LoginFixture.requestDto();

        mockMvc.perform(post("/logins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(service).login(request, response);
    }
}
