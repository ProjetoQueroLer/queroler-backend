package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoRequestDto;
import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;

import java.util.List;

public interface AcompanhamentoDeLeituraService {
    void adicionarComentario(Long diarioId, AcompanhamentoRequestDto dto);

    List<AcompanhamentoResponseDto> listarPorLivro(Long livroId);

    List<AcompanhamentoResponseDto> listarPorUsuario(Long usuarioId);
}
