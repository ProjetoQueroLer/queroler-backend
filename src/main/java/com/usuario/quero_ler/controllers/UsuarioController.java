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

    @GetMapping
    public ResponseEntity<UsuarioDadosResponse> dadosDoUsuario() {
        log.info("GET /usuarios - dadosDoUsuario - iniciando");
        UsuarioDadosResponse resp = serviceI.getDadosDoUsuario();
        log.info("GET /usuarios - dadosDoUsuario - concluído");
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/dados-adicionais")
    public ResponseEntity<Void> inserirDadosAdicionais(@RequestBody @Valid UsuarioDadosComplementarRequest dto) {
        log.info("PUT /usuarios/dados-adicionais - iniciando adição de dados complementares");
        serviceI.adicionarDados(dto);
        log.info("PUT /usuarios/dados-adicionais - dados adicionados");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid UsuarioAlterarSenhaRequest dto) {
        log.info("PUT /usuarios/alterar-senha - iniciando alteração de senha");
        serviceI.alterarSenha(dto);
        log.info("PUT /usuarios/alterar-senha - senha alterada");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<Void> alterar(@RequestBody @Valid UsuarioAtualizadoLeitorRequest dto) {
        log.info("PUT /usuarios - iniciar atualização (leitor)");
        serviceI.atualizar(dto);
        log.info("PUT /usuarios - atualização (leitor) concluída");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/administrador")
    public ResponseEntity<Void> alterar(@RequestBody @Valid UsuarioAtualizadoAdministradorRequest dto) {
        log.info("PUT /usuarios/administrador - iniciar atualização (administrador)");
        serviceI.atualizar(dto);
        log.info("PUT /usuarios/administrador - atualização (administrador) concluída");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirPerfil() {
        log.info("DELETE /usuarios - excluir perfil - iniciando");
        serviceI.excluirPerfil();
        log.info("DELETE /usuarios - excluir perfil - concluído");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/livro")
    public ResponseEntity<Void> adicionarLivro(@RequestParam Long idLivro,
            @RequestParam LivroStatus status) {

        log.info("POST /usuarios/livro - adicionar livro id={} status={}", idLivro, status);
        serviceI.adicionarLivro(idLivro, status);
        log.info("POST /usuarios/livro - livro adicionado");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/foto")
    public ResponseEntity<byte[]> buscarFoto() {
        log.info("GET /usuarios/foto - buscando foto do usuário");
        byte[] foto = serviceI.buscarFoto();
        log.info("GET /usuarios/foto - foto recuperada ({} bytes)", foto != null ? foto.length : 0);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(foto);
    }
}
