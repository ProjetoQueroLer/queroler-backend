package com.usuario.quero_ler.integrados;

import com.usuario.quero_ler.dtos.PageResponse;
import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.support.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/gerar_banco.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/limpar_banco.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NotificacaoTest extends AbstractIntegrationTest {
    @Autowired
    private TestRestTemplate template;

    private HttpHeaders authHeaders;

    @BeforeEach
    void setUp() {
        LoginRequestDto autenticacaoDto = new LoginRequestDto("leitor", "Teste123&");
        ResponseEntity<Void> loginResponse = template.postForEntity("/logins", autenticacaoDto, Void.class);
        authHeaders = new HttpHeaders();
        authHeaders.add(HttpHeaders.COOKIE, loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    @Test
    @DisplayName("Deve retornar as notificações de um usuario com sucesso!")
    public void deveRetornarAsNotificaçoesDeDeterminadoUsuarioComSucesso() {
        Long id = 2L;
        LocalDateTime agora = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        ResponseEntity<PageResponse<NotificacaoResponseDto>> resposta = template.exchange(
                "/notificacoes/{id}/usuario",
                HttpMethod.GET,
            new HttpEntity<>(authHeaders),
                new ParameterizedTypeReference<PageResponse<NotificacaoResponseDto>>() {
                }, id
        );
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody().content().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve marcar as notificações de um usuario como lidas!")
    public void deveMarcarAsNotificaçoesDeDeterminadoUsuarioComoLidas() {
        Long id = 2L;
        LocalDateTime agora = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        ResponseEntity<Void> resposta = template.exchange(
                "/notificacoes/{id}",
                HttpMethod.PUT,
            new HttpEntity<>(authHeaders), Void.class
                , id
        );
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}