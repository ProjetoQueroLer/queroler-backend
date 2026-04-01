package com.usuario.quero_ler.service.implementacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.fixtures.NotificacaoFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.NotificacaoMapper;
import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceImplTest {

    @InjectMocks
    private NotificacaoServiceImpl service;

    @Mock
    private NotificacaoRepository repository;

    @Mock
    private NotificacaoMapper mapper;

    @Mock
    private UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    @Mock
    private UsuarioServiceImpl usuarioService;

    @Test
    @DisplayName("Deve criar uma notificação para todos os usuarios do sistema")
    void criar() {
        NotificacaoRequestDto dto = NotificacaoFixture.requestDto();
        Notificacao notificacao = NotificacaoFixture.entity();
        NotificacaoResponseDto responseDto = NotificacaoFixture.response();

        when(mapper.toEntity(dto)).thenReturn(notificacao);
        when(repository.save(notificacao)).thenReturn(notificacao);
        when(mapper.toResponse(notificacao)).thenReturn(responseDto);

        NotificacaoResponseDto resposta = service.criar(dto);

        assertNotNull(resposta.id());
        assertEquals(dto.notificacao(),resposta.notificacao());
        assertEquals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                resposta.dataDeCriacao().truncatedTo(ChronoUnit.MINUTES));

        verify(usuarioNotificacaoRepository).enviarParaTodosUsuarios(notificacao.getId());
    }

    @Test
    @DisplayName("Deve marcar todas as notificaçoes de determinado usuario como lidas.")
    void deveRetornarNotificacoesNaoLidas() {

        Long idUsuario = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadePrincipal(user);
        usuario.setUser(user);

        Notificacao notificacao = NotificacaoFixture.entity();
        Notificacao notificacao2 = NotificacaoFixture.entity();

        List<Notificacao> lista = List.of(notificacao,notificacao2);

        when(usuarioService.getUsuario(idUsuario)).thenReturn(usuario);
        when(usuarioNotificacaoRepository.buscarNotificacoesNaoLidas(idUsuario))
                .thenReturn(lista);

        Page<NotificacaoResponseDto> resultado =
                service.naoLidas(idUsuario, pageable);

        assertEquals(2, resultado.getTotalElements());
        assertEquals(notificacao.getId(), resultado.getContent().get(0).id());
        assertEquals(notificacao2.getId(), resultado.getContent().get(1).id());

        verify(usuarioService).getUsuario(idUsuario);
        verify(usuarioNotificacaoRepository).buscarNotificacoesNaoLidas(idUsuario);
    }

    @Test
    @DisplayName("Deve marcar todas as notificaçoes do usuario como lidas")
    void deveMarcarNotificacoesDoUsuarioComoLidas() {
        Long idUsuario = 1L;

            User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
            Usuario usuario = UserFixture.entidadeCompleta(user);


            when(usuarioService.getUsuario(idUsuario)).thenReturn(usuario);

            service.marcarComoLidas(idUsuario);

            assertNotNull(usuario.getUser());
            verify(usuarioService).getUsuario(idUsuario);
            verify(usuarioNotificacaoRepository).marcarComoLidas(idUsuario);
        }

    @Test
    @DisplayName("Deve apagar as notificações cridas a mais de 30 dias")
    void apagarNotificacoesComMaisDe30Dias() {
        LocalDateTime dataRecorte = LocalDateTime.now().minusDays(30);
        service.apagarNotificacoesComMaisDe30Dias();

        verify(usuarioNotificacaoRepository).deleteByNotificacaoDataDeCriacaoBefore(dataRecorte);
        verify(repository).deleteByDataDeCriacaoBefore(dataRecorte);
    }
}
