package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificacaoServiceI {
    NotificacaoResponseDto criar(NotificacaoRequestDto dto);
    Page<NotificacaoResponseDto> naoLidas(Long id,Pageable pageable);
    void marcarComoLidas(Long idUsuario);

}