package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService serviceI;
    private final ObjectMapper mapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponseDto> criar(@RequestPart(value = "imagem", required = false) MultipartFile foto,
        @RequestPart("dados") String dadosJson) throws Exception {
        UsuarioRequestDto dto = mapper.readValue(dadosJson, UsuarioRequestDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceI.criar(dto,foto));
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

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid UsuarioAlterarSenhaRequest dto,
                                             @RequestHeader("Authorization") String token) {

        serviceI.alterarSenha(dto,token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizadoLeitorRequest dto) {
        serviceI.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/administrador")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizadoAdministradorRequest dto) {
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

    @GetMapping("/foto")
    public ResponseEntity<byte[]> buscarFoto(){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(serviceI.buscarFoto());
    }
}