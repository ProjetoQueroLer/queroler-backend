package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.mappers.NotificacaoMapper;
import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.models.UsuarioNotificacao;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.service.NotificacaoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificacaoServiceImpl implements NotificacaoService {
    private final NotificacaoRepository repository;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final NotificacaoMapper mapper;
    private final LoginService loginService;

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
    public Page<NotificacaoResponseDto> naoLidas(Pageable pageable) {
        apagarNotificacoesComMaisDe30Dias();
        Long idUsuario = loginService.getUsuarioLogado().getUsuario().getId();
        List<UsuarioNotificacao> usuarioNotificacaos = usuarioNotificacaoRepository.buscarTodasPorUsuario(idUsuario);
        List<NotificacaoResponseDto> notificacoes = new ArrayList<>();
        for (UsuarioNotificacao un : usuarioNotificacaos) {
            notificacoes.add(new NotificacaoResponseDto(
                    un.getNotificacao().getId(),
                    un.getNotificacao().getNotificacao(),
                    un.getNotificacao().getDataDeCriacao(),
                    un.getVisualizada()
            ));
        }
        Page<NotificacaoResponseDto> page = new PageImpl<>(notificacoes, pageable, notificacoes.size());
        return page;
    }

    @Transactional
    @Override
    public void marcarComoLidas() {
        apagarNotificacoesComMaisDe30Dias();
        Long idUsuario = loginService.getUsuarioLogado().getUsuario().getId();
        usuarioNotificacaoRepository.marcarComoLidas(idUsuario);
    }

    @Transactional
    public void apagarNotificacoesComMaisDe30Dias() {
        LocalDateTime dataDeCorte = LocalDateTime.now().minusDays(30).withNano(0);
        usuarioNotificacaoRepository.deleteByNotificacaoDataDeCriacaoBefore(dataDeCorte);
        repository.deleteByDataDeCriacaoBefore(dataDeCorte);
    }
}
