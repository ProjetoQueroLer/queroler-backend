package com.usuario.quero_ler.job;

import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoCleanupJob {

    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void executarLimpeza() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(30);

        long usuariosRemovidos = usuarioNotificacaoRepository.deleteByNotificacaoDataDeCriacaoBefore(dataLimite);

        long registrosRemovidos = notificacaoRepository.deleteByDataDeCriacaoBefore(dataLimite);

        log.info("Data limite: {}", dataLimite);
        log.info("usuario_notificacao removidos: {}", usuariosRemovidos);
        log.info("notificacoes removidas: {}", registrosRemovidos);
    }
}
