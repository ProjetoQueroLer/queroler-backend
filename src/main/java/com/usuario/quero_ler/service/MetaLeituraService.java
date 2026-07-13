package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;

public interface MetaLeituraService {

    void novaMeta(MetaRequestDto dto);
    void deletar();
}