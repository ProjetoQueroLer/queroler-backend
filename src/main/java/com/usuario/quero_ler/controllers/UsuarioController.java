package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.usuario.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.exceptions.especies.AusenciaDeDadosException;
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
        log.info("POST /usuarios - criar - iniciando");
        UsuarioRequestDto dto = mapper.readValue(dadosJson, UsuarioRequestDto.class);
        UsuarioResponseDto resp = serviceI.criar(dto, foto);
        log.info("POST /usuarios - criar - concluído para usuarioId={}", resp.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<UsuarioDadosResponse> dadosDoUsuario() {
        log.info("GET /usuarios - dadosDoUsuario - iniciando");
        UsuarioDadosResponse resp = serviceI.getDadosDoUsuario();
        log.info("GET /usuarios - dadosDoUsuario - concluído");
        return ResponseEntity.status(HttpStatus.OK).body(resp);
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

        log.info("PUT /usuarios/dados-adicionais - iniciando");
        serviceI.adicionarDados(dto, imagem);
        log.info("PUT /usuarios/dados-adicionais - concluído");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid UsuarioAlterarSenhaRequest dto) {
        log.info("PUT /usuarios/alterar-senha - iniciando alteração de senha");
        serviceI.alterarSenha(dto);
        log.info("PUT /usuarios/alterar-senha - senha alterada");
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

        log.info("PUT /usuarios - atualizar - iniciando");
        serviceI.atualizar(dto, imagem);
        log.info("PUT /usuarios - atualizar - concluído");
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

        log.info("PUT /usuarios/administrador - iniciando");
        serviceI.atualizar(dto, imagem);
        log.info("PUT /usuarios/administrador - concluído");
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
        log.info("POST /usuarios/livro - iniciando adicionarLivro usuarioId=?, livroId={}", idLivro);
        serviceI.adicionarLivro(idLivro, status);
        log.info("POST /usuarios/livro - concluído livroId={}", idLivro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/foto")
    public ResponseEntity<byte[]> buscarFoto() {
        log.info("GET /usuarios/foto - iniciando");
        byte[] foto = serviceI.buscarFoto();
        log.info("GET /usuarios/foto - concluído tamanho={}", foto != null ? foto.length : 0);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(foto);
    }
}
