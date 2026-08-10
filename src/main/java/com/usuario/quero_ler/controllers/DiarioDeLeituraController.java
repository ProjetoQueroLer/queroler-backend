package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;
import com.usuario.quero_ler.dtos.leitura.LivroAcompanhamentoResponseDto;
import jakarta.validation.Valid;
import com.usuario.quero_ler.service.DiarioDeLeituraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;

import java.util.List;

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

	@GetMapping("/acompanhamento")
	public ResponseEntity<List<LivroAcompanhamentoResponseDto>> listarEmAndamento() {
		return ResponseEntity.status(HttpStatus.OK).body(service.listarEmAndamento());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody DiarioDeLeituraAtualizadoRequest dto) {
		service.atualizar(id, dto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirDiarioDeLeitura(@PathVariable Long id) {
		service.excluirDiarioDeLeitura(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}