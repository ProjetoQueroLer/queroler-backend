package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.fixtures.NotificacaoFixture;
import com.usuario.quero_ler.models.Notificacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class NotificacaoMapperTest {

    @InjectMocks
    private NotificacaoMapper mapper;

    @Test
    @DisplayName("Deve converter uma notificação request e notificação entity")
    void toEntity() {
        NotificacaoRequestDto dto = NotificacaoFixture.requestDto();
        LocalDateTime agora = LocalDateTime.now();

        Notificacao resposta = mapper.toEntity(dto);

        assertNull(resposta.getId());
        assertEquals(dto.notificacao(),resposta.getNotificacao());
        assertEquals(agora, resposta.getDataDeCriacao());
    }

    @Test
    @DisplayName("Deve converter uma notificação em response")
    void toResponse() {
        Notificacao notificacao = NotificacaoFixture.entity();

        NotificacaoResponseDto resposta = mapper.toResponse(notificacao);

        assertEquals(notificacao.getId(),resposta.id());
        assertEquals(notificacao.getNotificacao(),resposta.notificacao());
        assertEquals(notificacao.getDataDeCriacao(), resposta.dataDeCriacao());
    }
}