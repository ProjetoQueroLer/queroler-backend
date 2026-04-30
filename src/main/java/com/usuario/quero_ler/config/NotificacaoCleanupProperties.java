package com.usuario.quero_ler.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.notificacao.cleanup")
public class NotificacaoCleanupProperties {
    private boolean enabled;
    private String cron;
    private String zone;
    private int retentionDays;
}
