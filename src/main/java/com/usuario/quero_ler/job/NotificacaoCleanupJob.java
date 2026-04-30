package com.usuario.quero_ler.job;

import com.usuario.quero_ler.config.NotificacaoCleanupProperties;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoCleanupJob {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final NotificacaoCleanupProperties properties;
    private final Clock clock;

    @Transactional
    @Scheduled(
            cron = "${app.notificacao.cleanup.cron}",
            zone = "${app.notificacao.cleanup.zone}"
    )
    public void executarLimpeza() {

        if (!properties.isEnabled()) {
            log.info("NotificacaoCleanupJob desabilitado");
            return;
        }

        ZoneId zoneId = ZoneId.of(properties.getZone());
        Clock zonedClock = clock.withZone(zoneId);

        LocalDateTime agora = LocalDateTime.now(zonedClock);
        LocalDateTime dataLimite = agora.minusDays(properties.getRetentionDays());

        long usuariosRemovidos =
                usuarioNotificacaoRepository.deleteByNotificacaoDataDeCriacaoBefore(dataLimite);

        long registrosRemovidos =
                notificacaoRepository.deleteByDataDeCriacaoBefore(dataLimite);

        log.info("Execução cleanup");
        log.info("Agora: {}", agora);
        log.info("Data limite: {}", dataLimite);
        log.info("usuario_notificacao removidos: {}", usuariosRemovidos);
        log.info("notificacoes removidas: {}", registrosRemovidos);
    }
}