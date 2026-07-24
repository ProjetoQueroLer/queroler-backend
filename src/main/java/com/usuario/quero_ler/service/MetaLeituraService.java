package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.dtos.meta.MetaResponseDto;

public interface MetaLeituraService {

    void novaMeta(MetaRequestDto dto);

    void atualizar(MetaRequestDto dto);

    void deletar();

    MetaResponseDto getMetas();
}