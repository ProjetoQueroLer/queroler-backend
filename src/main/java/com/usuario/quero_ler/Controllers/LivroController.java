package com.usuario.quero_ler.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usuario.quero_ler.dtos.livro.BuscaDeLivrosRequest;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.service.LivroServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/livros")
public class LivroController {
    private final LivroServiceI serviceI;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> cadastrar(
            @RequestPart(value = "imagem", required = false) MultipartFile capaDoLivro,
            @RequestPart("dados") String dadosJson) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        LivroRequest dto = mapper.readValue(dadosJson, LivroRequest.class);
        serviceI.criar(dto, capaDoLivro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    ResponseEntity<Page<LivroResponse>> listar(Pageable pageable){
        Page<LivroResponse> page = serviceI.listar(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/buscar/{isbn}")
    public ResponseEntity<LivroResponse> buscar(@PathVariable String isbn){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.buscarIsbn(isbn));
    }

    @GetMapping("/buscar/filtro")
    public ResponseEntity<Page<LivroResponse>> buscarFiltro(@RequestParam(required = false) String titulo,
                                                            @RequestParam(required = false) String editora,
                                                            @RequestParam(required = false) String autor,
                                                            Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.buscar(titulo,editora,autor,pageable));
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
}