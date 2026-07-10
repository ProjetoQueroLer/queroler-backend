package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.service.MetaLeituraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/metas")
public class MetaController {

    private final MetaLeituraService metaLeituraService;

    @PostMapping
    public ResponseEntity<Void> adicionar(@RequestBody @Valid MetaRequestDto dto) {
        metaLeituraService.novaMeta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}