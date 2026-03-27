package com.usuario.quero_ler.Controllers;

import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.service.DocumentoServiceI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/documentos")
public class DocumentoController {
    private final DocumentoServiceI serviceI;

    @PostMapping
    public ResponseEntity<DocumentoResponseDto> criar(@RequestBody @Valid DocumentoRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceI.criar(dto));
    }

    @GetMapping("/termos-gerais-de-uso")
    public ResponseEntity<DocumentoResponseDto> termosGeraisDeUso(){
        return ResponseEntity.status(HttpStatus.OK).body(serviceI.getTermosGeraisDeUso());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> alterar(@PathVariable Long id, @RequestBody @Valid DocumentoAlteracoesDto dto){
        serviceI.alterar(id, dto);
        return ResponseEntity.noContent().build();
    }
}