package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.fixtures.NotificacaoFixture;
import com.usuario.quero_ler.models.Notificacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class NotificacaoMapperTest {

    private final NotificacaoMapper mapper = new NotificacaoMapper();

    @Test
    @DisplayName("Deve converter uma notificação request em entity")
    void toEntity() {
        NotificacaoRequestDto dto = NotificacaoFixture.requestDto();

        LocalDateTime antes = LocalDateTime.now();

        Notificacao resposta = mapper.toEntity(dto);

        LocalDateTime depois = LocalDateTime.now();

        assertNull(resposta.getId());
        assertEquals(dto.notificacao(), resposta.getNotificacao());

        assertTrue(
                !resposta.getDataDeCriacao().isBefore(antes) &&
                        !resposta.getDataDeCriacao().isAfter(depois),
                "Data de criação deveria estar entre antes e depois"
        );
    }

    @Test
    @DisplayName("Deve converter uma notificação em response")
    void toResponse() {
        Notificacao notificacao = NotificacaoFixture.entity();

        NotificacaoResponseDto resposta = mapper.toResponse(notificacao);

        assertEquals(notificacao.getId(), resposta.id());
        assertEquals(notificacao.getNotificacao(), resposta.notificacao());

        assertEquals(
                notificacao.getDataDeCriacao().truncatedTo(ChronoUnit.SECONDS),
                resposta.dataDeCriacao().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}