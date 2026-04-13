package com.usuario.quero_ler.Controllers;

import com.usuario.quero_ler.dtos.livro.LivroCardResponse;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enuns.LivroStatus;
import com.usuario.quero_ler.service.UsuarioServiceI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioServiceI serviceI;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criar(@RequestBody @Valid UsuarioRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceI.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDadosResponse> dadosDoUsuario(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getDadosDoUsuario(id));
    }

    @PutMapping("/{id}/dados-adicionais")
    public ResponseEntity<Void> inserirDadosAdicionais(@PathVariable Long id, @RequestBody @Valid UsuarioDadosComplementarRequest dto) {
        serviceI.adicionarDados(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @RequestBody @Valid UsuarioAlterarSenhaReguest dto) {
        serviceI.alterarSenha(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizadoLeitorReguest dto) {
        serviceI.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/administrador")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizadoAdministradorReguest dto) {
        serviceI.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPerfil(@PathVariable Long id) {
        serviceI.excluirPerfil(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/livro")
    public ResponseEntity<Void> adicionarLivro(@PathVariable Long id,
                                               @RequestParam Long idLivro,
                                               @RequestParam LivroStatus status){

        serviceI.adicionarLivro(id, idLivro,status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}