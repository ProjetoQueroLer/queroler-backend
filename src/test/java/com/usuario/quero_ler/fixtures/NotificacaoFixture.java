package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.models.Notificacao;

import java.time.LocalDateTime;

public class NotificacaoFixture {
    private static final Long ID = 3L;
    private static final String NOTIFICACAO = "Alterado o termo gerais";
    private static final LocalDateTime DATA_DE_CRIACAO = LocalDateTime.now();

    public static NotificacaoRequestDto requestDto(){
        return  new NotificacaoRequestDto(NOTIFICACAO);
    }

    public static Notificacao entity(){
        return new Notificacao(ID, DATA_DE_CRIACAO,NOTIFICACAO);
    }

    public static NotificacaoResponseDto response(){
        return new NotificacaoResponseDto(ID,NOTIFICACAO,DATA_DE_CRIACAO);
    }
}