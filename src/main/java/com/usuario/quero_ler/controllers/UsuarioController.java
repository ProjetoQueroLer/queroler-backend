package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<UsuarioDadosResponse> dadosDoUsuario() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getDadosDoUsuario());
    }

    @PutMapping("/dados-adicionais")
    public ResponseEntity<Void> inserirDadosAdicionais(@RequestBody @Valid UsuarioDadosComplementarRequest dto) {
        serviceI.adicionarDados(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid UsuarioAlterarSenhaRequest dto) {
        serviceI.alterarSenha(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping
    public ResponseEntity<Void> alterar(@RequestBody @Valid UsuarioAtualizadoLeitorRequest dto) {
        serviceI.atualizar(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/administrador")
    public ResponseEntity<Void> alterar(@RequestBody @Valid UsuarioAtualizadoAdministradorRequest dto) {
        serviceI.atualizar(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirPerfil() {
        serviceI.excluirPerfil();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/livro")
    public ResponseEntity<Void> adicionarLivro(@RequestParam Long idLivro,
                                               @RequestParam LivroStatus status){

        serviceI.adicionarLivro(idLivro,status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/foto")
    public ResponseEntity<byte[]> buscarFoto(){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(serviceI.buscarFoto());
    }
}
