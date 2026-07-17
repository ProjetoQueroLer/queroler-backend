package com.usuario.quero_ler.service.implementacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.models.UsuarioNotificacao;
import com.usuario.quero_ler.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoResponseDto;
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.fixtures.NotificacaoFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.NotificacaoMapper;
import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.models.User;
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
    private LoginService loginService;

    @Mock
    private Clock clock;

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
    @DisplayName("Deve retornar todas as notificaçoes (lidas e não lidas) dos últimos 30 dias ordenadas da mais recente.")
    void deveRetornarNotificacoesNaoLidas() {

        Pageable pageable = PageRequest.of(0, 10);

        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadePrincipal(user);
        usuario.setUser(user);
        user.setUsuario(usuario);

        Notificacao notificacao = NotificacaoFixture.entity();
        Notificacao notificacao2 = NotificacaoFixture.entity();

        UsuarioNotificacao un1 = UsuarioNotificacao.builder()
                .usuario(usuario)
                .notificacao(notificacao)
                .visualizada(false)
                .build();

        UsuarioNotificacao un2 = UsuarioNotificacao.builder()
                .usuario(usuario)
                .notificacao(notificacao2)
                .visualizada(true)
                .build();

        List<UsuarioNotificacao> lista = List.of(un1, un2);

        when(loginService.getUsuarioLogado()).thenReturn(user);
        when(usuarioNotificacaoRepository.buscarTodasPorUsuario(usuario.getId()))
                .thenReturn(lista);

        Page<NotificacaoResponseDto> resultado =
                service.naoLidas(pageable);

        assertEquals(2, resultado.getTotalElements());
        assertEquals(notificacao.getId(), resultado.getContent().get(0).id());
        assertEquals(notificacao2.getId(), resultado.getContent().get(1).id());
        assertEquals(false, resultado.getContent().get(0).visualizada());
        assertEquals(true, resultado.getContent().get(1).visualizada());

        verify(loginService).getUsuarioLogado();
        verify(usuarioNotificacaoRepository).buscarTodasPorUsuario(usuario.getId());
    }

    @Test
    @DisplayName("Deve marcar todas as notificaçoes do usuario como lidas")
    void deveMarcarNotificacoesDoUsuarioComoLidas() {
            User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
            Usuario usuario = UserFixture.entidadeCompleta(user);
            user.setUsuario(usuario);

            when(loginService.getUsuarioLogado()).thenReturn(user);

            service.marcarComoLidas();

            assertNotNull(usuario.getUser());
            verify(loginService).getUsuarioLogado();
            verify(usuarioNotificacaoRepository).marcarComoLidas(usuario.getId());
        }

    @Test
    @DisplayName("Deve apagar as notificações cridas a mais de 30 dias")
    void apagarNotificacoesComMaisDe30Dias() {
        LocalDateTime dataRecorte = LocalDateTime.now().minusDays(30).withNano(0);
        service.apagarNotificacoesComMaisDe30Dias();

        verify(usuarioNotificacaoRepository).deleteByNotificacaoDataDeCriacaoBefore(dataRecorte);
        verify(repository).deleteByDataDeCriacaoBefore(dataRecorte);
    }




    @Test
    @DisplayName("Deve gerar notificações aos usuarios surgerindo a criação de novas metas para o ano que se inicia")
    void deveGerarNotificacoesComSugestaoDeCriacaoDeNovasMetasParaOAno() {
        LocalDate hoje = LocalDate.now().plusYears(1);
        LocalDate data = LocalDate.of(hoje.getYear(), 1, 1);
        String mensagem = "Olá. Que tal revisar suas metas de leituras para o novo ano que se inicia?";

        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        Notificacao notificacao = NotificacaoFixture.entity();
        notificacao.setId(1L);

        NotificacaoResponseDto responseDto = NotificacaoFixture.response();

        when(repository.existsByDataAndNotificacaoIgnoreCase(data, mensagem)).thenReturn(false);
        when(mapper.toEntity(any(NotificacaoRequestDto.class))).thenReturn(notificacao);
        when(repository.save(notificacao)).thenReturn(notificacao);
        when(mapper.toResponse(notificacao)).thenReturn(responseDto);

        service.geraNotificacaoParaRenovacaoDeMeta();

        verify(repository).existsByDataAndNotificacaoIgnoreCase(data, mensagem);
        verify(mapper).toEntity(any(NotificacaoRequestDto.class));
        verify(repository).save(notificacao);
        verify(usuarioNotificacaoRepository).enviarParaTodosUsuarios(notificacao.getId());
        verify(mapper).toResponse(notificacao);
    }

    @Test
    @DisplayName("Não deve gerar notificações aos usuarios surgerindo a criação de novas metas para o ano que se inicia")
    void deveEnviarNotificacoesComSugestaoDeCriacaoDeNovasMetasParaOAno() {
        LocalDate hoje = LocalDate.now().plusYears(1);
        LocalDate data = LocalDate.of(hoje.getYear(), 11, 1);

        Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        service.geraNotificacaoParaRenovacaoDeMeta();

        verifyNoInteractions(repository);
        verifyNoInteractions(mapper);
        verifyNoInteractions(repository);
        verifyNoInteractions(usuarioNotificacaoRepository);
    }
}
