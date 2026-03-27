package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.models.Notificacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class NotificacaoMapper {

    public Notificacao toEntity(NotificacaoRequestDto dto){
        Notificacao notificacao = new Notificacao();
        notificacao.setNotificacao(dto.notificacao());
        notificacao.setDataDeCriacao(LocalDateTime.now());
        return notificacao;
    }

    public NotificacaoResponseDto toResponse(Notificacao notificacao){
        return new NotificacaoResponseDto(
                notificacao.getId(),
                notificacao.getNotificacao(),
                notificacao.getDataDeCriacao()
        );
    }
}
