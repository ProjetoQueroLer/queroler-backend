package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;
import jakarta.validation.Valid;
import com.usuario.quero_ler.service.DiarioDeLeituraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/diario")
public class DiarioDeLeituraController {

	private final DiarioDeLeituraService service;

	@PostMapping
	public ResponseEntity<Void> criar(@RequestBody @Valid DiarioDeLeituraRequestDto dto) {
		service.criar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<DiarioDeLeituraResponseDto> buscarDiarioDeLeitura(@RequestParam Long livroId) {
		DiarioDeLeituraResponseDto response = service.buscarLeituraPorLivroEUsuario(livroId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody DiarioDeLeituraAtualizadoRequest dto) {
		service.atualizar(id, dto);
		return ResponseEntity.noContent().build();
	}
}
