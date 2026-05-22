package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoRequestDto;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/leituras")
public class AcompanhamentoDeLeituraController {

    private final AcompanhamentoDeLeituraService acompanhamentoService;

    @PostMapping("/{diarioId}/comentarios")
    public ResponseEntity<Void> criarComentario(@PathVariable Long diarioId,
            @RequestBody @Valid AcompanhamentoRequestDto dto) {
        acompanhamentoService.adicionarComentario(diarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
