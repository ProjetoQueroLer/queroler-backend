package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.mappers.NotificacaoMapper;
import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.service.NotificacaoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificacaoServiceImpl implements NotificacaoService {
    private final NotificacaoRepository repository;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final NotificacaoMapper mapper;
    private final LoginService loginService;

    @Transactional
    @Override
    public NotificacaoResponseDto criar(NotificacaoRequestDto dto) {
        log.info("NotificacaoServiceImpl.criar - texto={}", dto.notificacao());
        Notificacao notificacao = mapper.toEntity(dto);
        notificacao = repository.save(notificacao);
        usuarioNotificacaoRepository.enviarParaTodosUsuarios(notificacao.getId());
        log.debug("Notificacao salva id={}", notificacao.getId());
        log.info("NotificacaoServiceImpl.criar - concluído id={}", notificacao.getId());
        return mapper.toResponse(notificacao);
    }

    @Transactional
    @Override
    public Page<NotificacaoResponseDto> naoLidas(Pageable pageable) {
        apagarNotificacoesComMaisDe30Dias();
        Long idUsuario = loginService.getUsuarioLogado().getUsuario().getId();
        log.info("NotificacaoServiceImpl.naoLidas - iniciando idUsuario={} page={} size={}", idUsuario,
                pageable.getPageNumber(), pageable.getPageSize());
        List<Notificacao> usuarioNotificacaos = usuarioNotificacaoRepository.buscarNotificacoesNaoLidas(idUsuario);
        List<NotificacaoResponseDto> notificacoes = new ArrayList<>();
        for (Notificacao notificacao : usuarioNotificacaos) {
            notificacoes.add(new NotificacaoResponseDto(notificacao.getId(), notificacao.getNotificacao(),
                    notificacao.getDataDeCriacao()));
        }
        Page<NotificacaoResponseDto> page = new PageImpl<>(notificacoes, pageable, notificacoes.size());
        log.info("NotificacaoServiceImpl.naoLidas - concluído idUsuario={} count={}", idUsuario, notificacoes.size());
        return page;
    }

    @Transactional
    @Override
    public void marcarComoLidas() {
        apagarNotificacoesComMaisDe30Dias();
        Long idUsuario = loginService.getUsuarioLogado().getUsuario().getId();
        log.info("NotificacaoServiceImpl.marcarComoLidas - iniciando idUsuario={}", idUsuario);
        usuarioNotificacaoRepository.marcarComoLidas(idUsuario);
        log.info("NotificacaoServiceImpl.marcarComoLidas - concluído idUsuario={}", idUsuario);
    }

    @Transactional
    public void apagarNotificacoesComMaisDe30Dias() {
        LocalDateTime dataDeCorte = LocalDateTime.now().minusDays(30);
        log.debug("NotificacaoServiceImpl.apagarNotificacoesComMaisDe30Dias - iniciando dataDeCorte={}", dataDeCorte);
        usuarioNotificacaoRepository.deleteByNotificacaoDataDeCriacaoBefore(dataDeCorte);
        repository.deleteByDataDeCriacaoBefore(dataDeCorte);
        log.debug("NotificacaoServiceImpl.apagarNotificacoesComMaisDe30Dias - concluído dataDeCorte={}", dataDeCorte);
    }
}
