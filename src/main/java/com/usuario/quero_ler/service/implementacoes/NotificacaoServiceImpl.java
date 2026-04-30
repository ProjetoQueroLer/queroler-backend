package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.mappers.NotificacaoMapper;
import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.service.NotificacaoService;
import com.usuario.quero_ler.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificacaoServiceImpl implements NotificacaoService {

    private final NotificacaoRepository repository;
    private final UsuarioService usuarioService;
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

    @Transactional(readOnly = true)
    @Override
    public Page<NotificacaoResponseDto> naoLidas(Long idUsuario, Pageable pageable) {

        usuarioService.getUsuario(idUsuario);

        List<NotificacaoResponseDto> notificacoes = usuarioNotificacaoRepository.buscarNotificacoesNaoLidas(idUsuario).stream().map(mapper::toResponse).toList();

        return new PageImpl<>(notificacoes, pageable, notificacoes.size());
    }

    @Transactional
    @Override
    public void marcarComoLidas(Long idUsuario) {

        usuarioService.getUsuario(idUsuario);

        usuarioNotificacaoRepository.marcarComoLidas(idUsuario);
    }
}