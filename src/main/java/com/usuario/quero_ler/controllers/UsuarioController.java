package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService serviceI;
    private final ObjectMapper mapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponseDto> criar(@RequestPart(value = "imagem", required = false) MultipartFile foto,
            @RequestPart("dados") String dadosJson) throws Exception {
        UsuarioRequestDto dto = mapper.readValue(dadosJson, UsuarioRequestDto.class);
        log.info("POST /usuarios - criar usuario: {}", dto.email());
        UsuarioResponseDto resp = serviceI.criar(dto, foto);
        log.info("Usuario criado: id={}", resp != null ? resp.id() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDadosResponse> dadosDoUsuario(@PathVariable Long id) {
        log.info("GET /usuarios/{} - dados do usuario", id);
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getDadosDoUsuario(id));
    }

    @PutMapping("/{id}/dados-adicionais")
    public ResponseEntity<Void> inserirDadosAdicionais(@PathVariable Long id,
            @RequestBody @Valid UsuarioDadosComplementarRequest dto) {
        log.info("PUT /usuarios/{}/dados-adicionais - inserir dados", id);
        serviceI.adicionarDados(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id,
            @RequestBody @Valid UsuarioAlterarSenhaRequest dto) {
        log.info("PUT /usuarios/{}/alterar-senha", id);
        serviceI.alterarSenha(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid UsuarioAtualizadoLeitorRequest dto) {
        log.info("PUT /usuarios/{} - atualizar leitor", id);
        serviceI.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/administrador")
    public ResponseEntity<Void> alterar(@PathVariable Long id,
            @RequestBody @Valid UsuarioAtualizadoAdministradorRequest dto) {
        log.info("PUT /usuarios/{} - atualizar administrador", id);
        serviceI.atualizar(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPerfil(@PathVariable Long id) {
        log.info("DELETE /usuarios/{} - excluir perfil", id);
        serviceI.excluirPerfil(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/livro")
    public ResponseEntity<Void> adicionarLivro(@PathVariable Long id,
            @RequestParam Long idLivro,
            @RequestParam LivroStatus status) {
        log.info("POST /usuarios/{}/livro - adicionar livro {} status={}", id, idLivro, status);
        serviceI.adicionarLivro(id, idLivro, status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/foto")
    public ResponseEntity<byte[]> buscarFoto() {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(serviceI.buscarFoto());
    }
}