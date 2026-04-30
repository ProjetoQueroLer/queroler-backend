package com.usuario.quero_ler.job;

import com.usuario.quero_ler.config.NotificacaoCleanupProperties;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Job de limpeza de notificações")
class NotificacaoCleanupJobTest {

    private NotificacaoRepository notificacaoRepository;
    private UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private NotificacaoCleanupProperties properties;
    private Clock clock;
    private NotificacaoCleanupJob job;

    @BeforeEach
    void setup() {
        notificacaoRepository = mock(NotificacaoRepository.class);
        usuarioNotificacaoRepository = mock(UsuarioNotificacaoRepository.class);

        properties = new NotificacaoCleanupProperties();
        properties.setEnabled(true);
        properties.setRetentionDays(30);
        properties.setZone("America/Sao_Paulo");

        clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneId.of("UTC"));

        job = new NotificacaoCleanupJob(notificacaoRepository, usuarioNotificacaoRepository, properties, clock);
    }

    @Test
    @DisplayName("Deve deletar notificações antigas em ambas as tabelas com data correta")
    void deveDeletarNotificacoesAntigasComDataCorreta() {

        when(usuarioNotificacaoRepository.deleteByNotificacaoDataDeCriacaoBefore(any())).thenReturn(5L);
        when(notificacaoRepository.deleteByDataDeCriacaoBefore(any())).thenReturn(3L);

        job.executarLimpeza();

        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(usuarioNotificacaoRepository).deleteByNotificacaoDataDeCriacaoBefore(captor.capture());

        LocalDateTime dataCapturada = captor.getValue();

        ZoneId zone = ZoneId.of(properties.getZone());

        LocalDateTime esperado = LocalDateTime.ofInstant(clock.instant(), zone).minusDays(properties.getRetentionDays());

        assertEquals(esperado, dataCapturada);

        verify(notificacaoRepository).deleteByDataDeCriacaoBefore(esperado);
    }

    @Test
    @DisplayName("Deve executar a deleção na ordem correta (filho antes do pai)")
    void deveExecutarNaOrdemCorreta() {

        job.executarLimpeza();

        InOrder inOrder = inOrder(usuarioNotificacaoRepository, notificacaoRepository);

        inOrder.verify(usuarioNotificacaoRepository).deleteByNotificacaoDataDeCriacaoBefore(any());

        inOrder.verify(notificacaoRepository).deleteByDataDeCriacaoBefore(any());
    }

    @Test
    @DisplayName("Não deve executar limpeza quando estiver desabilitado")
    void naoDeveExecutarQuandoDesabilitado() {

        properties.setEnabled(false);

        job.executarLimpeza();

        verifyNoInteractions(notificacaoRepository);
        verifyNoInteractions(usuarioNotificacaoRepository);
    }
}