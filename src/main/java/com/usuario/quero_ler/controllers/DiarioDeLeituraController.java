package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.service.DiarioDeLeituraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/leituras")
public class DiarioDeLeituraController {

    private final DiarioDeLeituraService service;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody DiarioDeLeituraRequestDto dto) {
        service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
