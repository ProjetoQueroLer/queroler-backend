package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criar(@RequestBody @Valid UsuarioRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDadosResponse> dadosDoUsuario(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getDadosDoUsuario(id));
    }

    @PutMapping("/{id}/dados-adicionais")
    public ResponseEntity<Void> inserirDadosAdicionais(@PathVariable Long id, @RequestBody @Valid UsuarioDadosComplementarRequest dto) {
        service.adicionarDados(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @RequestBody @Valid UsuarioAlterarSenhaRequest dto) {
        service.alterarSenha(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizadoLeitorRequest dto) {
        service.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/administrador")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizadoAdministradorRequest dto) {
        service.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPerfil(@PathVariable Long id) {
        service.excluirPerfil(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/livro")
    public ResponseEntity<Void> adicionarLivro(@PathVariable Long id,
                                               @RequestParam Long idLivro,
                                               @RequestParam LivroStatus status){

        service.adicionarLivro(id, idLivro,status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}