package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {
    private final NotificacaoService serviceI;

    @GetMapping
    public ResponseEntity<Page<NotificacaoResponseDto>> naoLidas(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.naoLidas(pageable));
    }

    @PutMapping
    ResponseEntity<Void> marcarComoLidas() {
        serviceI.marcarComoLidas();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
