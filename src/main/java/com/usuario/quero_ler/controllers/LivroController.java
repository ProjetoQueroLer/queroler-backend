package com.usuario.quero_ler.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.livro.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;
import java.util.List;
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
    private final LivroService service;
    private final AcompanhamentoDeLeituraService acompanhamentoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> cadastrar(
            @RequestPart(value = "imagem", required = false) MultipartFile capaDoLivro,
            @RequestPart("dados") String dadosJson) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        LivroRequest dto = mapper.readValue(dadosJson, LivroRequest.class);
        service.criar(dto, capaDoLivro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<Page<LivroCardResponse>> buscar(@RequestParam(required = false) String titulo,
            @RequestParam(required = false) String editora,
            @RequestParam(required = false) String autor,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscar(titulo, editora, autor, pageable));
    }

    @GetMapping("/populares")
    ResponseEntity<Page<LivroResponse>> listarPopulares() {
        Page<LivroResponse> page = service.listarPopulares();
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/buscar/{isbn}")
    public ResponseEntity<LivroResponse> buscar(@PathVariable String isbn) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarIsbn(isbn));
    }

    @GetMapping("/{id}/capa")
    public ResponseEntity<byte[]> buscarCapa(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(service.buscarCapa(id));
    }

    @PutMapping("/{id}/capa")
    public ResponseEntity<Void> inserirCapa(
            @PathVariable Long id,
            @RequestPart(value = "imagem") MultipartFile capaDoLivro) {

        service.inserirCapaDoLivro(id, capaDoLivro);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/usuario")
    public ResponseEntity<Void> mudarStatus(@PathVariable Long id,
            @RequestParam LivroStatus status) {
        service.alterarStatusDoLivroNoUsuario(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tela_de_leitura")
    public ResponseEntity<Page<LivroTelaLeituraResponse>> livrosDoUsuarioParaTelaDeLeitura(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getLivrosTelaDeLeituraDoUsuario(pageable));
    }

    @GetMapping("/detalhados")
    public ResponseEntity<Page<LivroDetalhadoResponse>> getLivrosDetalhadosDoUsuario(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getLivrosDoUsuario(pageable));
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<AcompanhamentoResponseDto>> listarComentariosPorLivro(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(acompanhamentoService.listarPorLivro(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
