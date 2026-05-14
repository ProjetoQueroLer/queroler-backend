package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.service.DocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/documentos")
public class DocumentoController {
    private final DocumentoService serviceI;

    @PostMapping
    public ResponseEntity<DocumentoResponseDto> criar(@RequestBody @Valid DocumentoRequestDto dto) {
        log.info("POST /documentos - criar documento: {}", dto);
        DocumentoResponseDto resp = serviceI.criar(dto);
        log.info("Documento criado: id={}", resp != null ? resp.id() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/termos-gerais-de-uso")
    public ResponseEntity<DocumentoResponseDto> termosGeraisDeUso() {
        log.info("GET /documentos/termos-gerais-de-uso");
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getTermosGeraisDeUso());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid DocumentoAlteracoesDto dto) {
        log.info("PUT /documentos/{} - alterar documento", id);
        serviceI.alterar(id, dto);
        return ResponseEntity.noContent().build();
    }
}