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

    @GetMapping("/{id}/usuario")
    public ResponseEntity<Page<NotificacaoResponseDto>> naoLidas(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.naoLidas(id, pageable));
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> marcarComoLidas(@PathVariable Long id) {
        serviceI.marcarComoLidas(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}