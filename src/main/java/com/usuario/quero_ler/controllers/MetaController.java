package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.dtos.meta.MetaResponseDto;
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

    @PutMapping
    public ResponseEntity<Void> atualizar(@RequestBody @Valid MetaRequestDto dto) {
        metaLeituraService.atualizar(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar() {
        metaLeituraService.deletar();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<MetaResponseDto> getMetas() {
        return ResponseEntity.status(HttpStatus.OK).body(metaLeituraService.getMetas());
    }

    @PutMapping("/adicionar-livro/{id}")
    public ResponseEntity<Void> adicionarLivro(@PathVariable Long id){
        metaLeituraService.adicionarLivro(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}