package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.livro.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/livros")
public class LivroController {
    private final LivroService serviceI;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> cadastrar(
            @RequestPart(value = "imagem", required = false) MultipartFile capaDoLivro,
            @RequestPart("dados") String dadosJson) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        LivroRequest dto = mapper.readValue(dadosJson, LivroRequest.class);
        serviceI.criar(dto, capaDoLivro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<Page<LivroCardResponse>> buscar(@RequestParam(required = false) String titulo,
                                                            @RequestParam(required = false) String editora,
                                                            @RequestParam(required = false) String autor,
                                                            Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.buscar(titulo,editora,autor,pageable));
    }

    @GetMapping("/populares")
    ResponseEntity<Page<LivroResponse>> listarPopulares(){
        Page<LivroResponse> page = serviceI.listarPopulares();
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/buscar/{isbn}")
    public ResponseEntity<LivroResponse> buscar(@PathVariable String isbn){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.buscarIsbn(isbn));
    }

    @GetMapping("/{id}/capa")
    public ResponseEntity<byte[]> buscarCapa(@PathVariable Long id){
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(serviceI.buscarCapa(id));
    }

    @PutMapping("/{id}/capa")
    public ResponseEntity<Void> inserirCapa(
            @PathVariable Long id,
            @RequestPart(value = "imagem") MultipartFile capaDoLivro) {

        serviceI.inserirCapaDoLivro(id, capaDoLivro);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/usuario/{idUsuario}")
    public ResponseEntity<Void> mudarStatus(@PathVariable Long id,  @PathVariable Long idUsuario,
                                            @RequestParam LivroStatus status) {
        serviceI.alterarStatusDoLivroNoUsuario(id,idUsuario, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tela_de_leitura/usuario/{id}")
    public ResponseEntity<Page<LivroTelaLeituraResponse>> livrosDoUsuarioParaTelaDeLeitura(@PathVariable Long id, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getLivrosTelaDeLeituraDoUsuario(id, pageable));
    }

    @GetMapping("/detalhados/usuario/{id}")
    public ResponseEntity<Page<LivroDetalhadoResponse>> getLivrosDetalhadosDoUsuario(@PathVariable Long id, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getLivrosDoUsuario(id,pageable));
    }
}