package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;

public interface DiarioDeLeituraService {
    void criar(DiarioDeLeituraRequestDto dto);

    void atualizar(Long id, DiarioDeLeituraAtualizadoRequest dto);
}
