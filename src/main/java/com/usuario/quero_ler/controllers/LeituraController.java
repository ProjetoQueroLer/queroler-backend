package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.leitura.AdicionarLeituraRequestDto;
import jakarta.validation.Valid;
import com.usuario.quero_ler.service.LeituraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/leituras")
public class LeituraController {

    private final LeituraService leituraService;

    @PostMapping
    public ResponseEntity<Void> adicionar(@RequestBody @Valid AdicionarLeituraRequestDto dto) {
        leituraService.adicionar(dto.livroId(), dto.status());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
