package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;

public interface DiarioDeLeituraService {
    void criar(DiarioDeLeituraRequestDto dto);
	DiarioDeLeituraResponseDto buscarLeituraPorLivroEUsuario(Long livroId);
    void atualizar(Long id, DiarioDeLeituraAtualizadoRequest dto);
}
