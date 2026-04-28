package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.mappers.NotificacaoMapper;
import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.service.NotificacaoServiceI;
import com.usuario.quero_ler.service.UsuarioServiceI;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificacaoServiceImpl implements NotificacaoServiceI {
    private final NotificacaoRepository repository;
    private final UsuarioServiceI usuarioServiceI;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final NotificacaoMapper mapper;

    @Transactional
    @Override
    public NotificacaoResponseDto criar(NotificacaoRequestDto dto) {
        Notificacao notificacao = mapper.toEntity(dto);
        notificacao = repository.save(notificacao);
        usuarioNotificacaoRepository.enviarParaTodosUsuarios(notificacao.getId());
        return mapper.toResponse(notificacao);
    }

    @Transactional
    @Override
    public Page<NotificacaoResponseDto> naoLidas(Long idUsuario, Pageable pageable) {
        List<Notificacao> usuarioNotificacaos = usuarioNotificacaoRepository.buscarNotificacoesNaoLidas(idUsuario);

        List<NotificacaoResponseDto> notificacoes = new ArrayList<>();

        for (Notificacao notificacao : usuarioNotificacaos) {
            notificacoes.add(new NotificacaoResponseDto(notificacao.getId(), notificacao.getNotificacao(), notificacao.getDataDeCriacao()));
        }

        return new PageImpl<>(notificacoes, pageable, notificacoes.size());
    }

    @Transactional
    @Override
    public void marcarComoLidas(Long idUsuario) {
        usuarioServiceI.getUsuario(idUsuario);
        usuarioNotificacaoRepository.marcarComoLidas(idUsuario);
    }
}