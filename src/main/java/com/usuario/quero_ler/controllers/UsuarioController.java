package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LeituraStatus;
import com.usuario.quero_ler.exceptions.especies.AusenciaDeDadosException;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
import com.usuario.quero_ler.service.UsuarioService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    private final ObjectMapper mapper;
    private final Validator validator;
    private final AcompanhamentoDeLeituraService acompanhamentoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponseDto> criar(
            @RequestPart(value = "imagem", required = false) MultipartFile foto,
            @RequestPart("dados") String dadosJson) throws Exception {

        UsuarioRequestDto dto = mapper.readValue(dadosJson, UsuarioRequestDto.class);

        var violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(dto, foto));
    }

    @GetMapping
    public ResponseEntity<UsuarioResponseDto> dadosDoUsuario() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getDadosDoUsuario());
    }

    @PutMapping(value = "/dados-adicionais", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> inserirDadosAdicionais(@RequestPart(value = "dados", required = false) String dados,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) throws JsonProcessingException {

        UsuarioDadosComplementarRequest dto = null;

        if (dados == null && (imagem == null || imagem.isEmpty())) {
            throw new AusenciaDeDadosException("É necessário enviar dados ou imagem.");
        }

        if (dados != null) {
            dto = mapper.readValue(dados, UsuarioDadosComplementarRequest.class);
        }

        service.adicionarDados(dto, imagem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid UsuarioAlterarSenhaRequest dto) {
        service.alterarSenha(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> alterar(@RequestPart(value = "dados", required = false) String dados,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) throws JsonProcessingException {

        UsuarioAtualizadoLeitorRequest dto = null;

        if (dados == null && (imagem == null || imagem.isEmpty())) {
            throw new AusenciaDeDadosException("É necessário enviar dados ou imagem.");
        }

        if (dados != null) {
            dto = mapper.readValue(dados, UsuarioAtualizadoLeitorRequest.class);
        }

        service.atualizar(dto, imagem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/administrador", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> alterarAdministrador(@RequestPart(value = "dados", required = false) String dados,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) throws JsonProcessingException {

        UsuarioAtualizadoAdministradorRequest dto = null;

        if (dados == null && (imagem == null || imagem.isEmpty())) {
            throw new AusenciaDeDadosException("É necessário enviar dados ou imagem.");
        }

        if (dados != null) {
            dto = mapper.readValue(dados, UsuarioAtualizadoAdministradorRequest.class);
        }

        service.atualizar(dto, imagem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirPerfil() {
        service.excluirPerfil();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/foto")
    public ResponseEntity<byte[]> buscarFoto() {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(service.buscarFoto());
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<AcompanhamentoResponseDto>> listarComentariosPorUsuario(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(acompanhamentoService.listarPorUsuario(id));
    }
}
