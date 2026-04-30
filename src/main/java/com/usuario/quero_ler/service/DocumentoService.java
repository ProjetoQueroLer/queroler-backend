package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;

public interface DocumentoService {
    DocumentoResponseDto criar(DocumentoRequestDto dto);
    void alterar(Long id, DocumentoAlteracoesDto dto);
    DocumentoResponseDto getTermosGeraisDeUso();
}
