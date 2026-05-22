package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/notificacoes")
public class NotificacaoController {
    private final NotificacaoService serviceI;

    @GetMapping
    public ResponseEntity<Page<NotificacaoResponseDto>> naoLidas(Pageable pageable) {
        log.info("GET /notificacoes - listar notificações não lidas. página: {}", pageable.getPageNumber());
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.naoLidas(pageable));
    }

    @PutMapping
    ResponseEntity<Void> marcarComoLidas() {
        log.info("PUT /notificacoes - marcar como lidas");
        serviceI.marcarComoLidas();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
