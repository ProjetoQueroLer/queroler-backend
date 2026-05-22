package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoRequestDto;

public interface AcompanhamentoDeLeituraService {
    void adicionarComentario(Long diarioId, AcompanhamentoRequestDto dto);
}
