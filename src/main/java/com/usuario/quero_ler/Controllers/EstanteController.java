package com.usuario.quero_ler.Controllers;

import com.usuario.quero_ler.dtos.livro.LivroCardResponse;
import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enuns.LivroStatus;
import com.usuario.quero_ler.service.EstanteDoUsuarioServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/estante")
public class EstanteController {

    private final EstanteDoUsuarioServiceI serviceI;

    @PostMapping()
    public ResponseEntity<LivroCardResponse> adicionar(@RequestParam(required = false) Long idUsuario,
                                                   @RequestParam(required = false) Long idLivro){

        serviceI.adicionar(idUsuario, idLivro);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<LivroTelaLeituraResponse>> livrosQueQueroLer(@PathVariable Long id, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.lista(id, pageable));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> mudarStatus(
            @PathVariable Long id,
            @RequestParam String isbn,
            @RequestParam LivroStatus status) {

        serviceI.mudarStatus(id, isbn, status);
        return ResponseEntity.ok().build();
    }

}
