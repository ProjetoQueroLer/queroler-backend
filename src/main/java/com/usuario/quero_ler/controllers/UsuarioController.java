package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceI.criar(dto, foto));
    }

    @GetMapping
    public ResponseEntity<UsuarioDadosResponse> dadosDoUsuario() {
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getDadosDoUsuario());
    }

    @PutMapping(value = "/dados-adicionais", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> inserirDadosAdicionais(@RequestPart(value = "dados", required = false) String dados,
                                                         @RequestPart(value = "imagem", required = false) MultipartFile imagem) throws JsonProcessingException {

        UsuarioDadosComplementarRequest dto= null;

        if (dados == null && (imagem == null || imagem.isEmpty())) {
            throw new IllegalArgumentException("É necessário enviar dados ou imagem.");
        }

        if (dados != null){
            dto = mapper.readValue(dados, UsuarioDadosComplementarRequest.class);
        }

        serviceI.adicionarDados(dto, imagem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid UsuarioAlterarSenhaRequest dto) {
        serviceI.alterarSenha(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> alterar(@RequestPart(value = "dados", required = false) String dados,
                                        @RequestPart(value = "imagem", required = false) MultipartFile imagem) throws JsonProcessingException {

        UsuarioAtualizadoLeitorRequest dto = null;

        if (dados == null && (imagem == null || imagem.isEmpty())) {
            throw new IllegalArgumentException("É necessário enviar dados ou imagem.");
        }

        if(dados!= null){
            dto = mapper.readValue(dados, UsuarioAtualizadoLeitorRequest.class);
        }

        serviceI.atualizar(dto, imagem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/administrador", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> alterarAdministrador(@RequestPart(value = "dados", required = false) String dados,
                                        @RequestPart(value = "imagem", required = false) MultipartFile imagem) throws JsonProcessingException {

        UsuarioAtualizadoAdministradorRequest dto = null;

        if (dados == null && (imagem == null || imagem.isEmpty())) {
            throw new IllegalArgumentException("É necessário enviar dados ou imagem.");
        }

        if(dados != null){
            dto = mapper.readValue(dados, UsuarioAtualizadoAdministradorRequest.class);
        }

        serviceI.atualizar(dto,imagem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirPerfil() {
        serviceI.excluirPerfil();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/livro")
    public ResponseEntity<Void> adicionarLivro(@RequestParam Long idLivro,
                                               @RequestParam LivroStatus status) {

        serviceI.adicionarLivro(idLivro, status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/foto")
    public ResponseEntity<byte[]> buscarFoto() {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(serviceI.buscarFoto());
    }
}
