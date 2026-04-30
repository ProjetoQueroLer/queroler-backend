package com.usuario.quero_ler.service.implementacoes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;

import com.usuario.quero_ler.models.Usuario;
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
import com.usuario.quero_ler.enums.UsuarioProfile;
import com.usuario.quero_ler.fixtures.NotificacaoFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.NotificacaoMapper;
import com.usuario.quero_ler.models.Notificacao;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.repository.NotificacaoRepository;
import com.usuario.quero_ler.repository.UsuarioNotificacaoRepository;
import com.usuario.quero_ler.service.UsuarioService;

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
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve criar uma notificação para todos os usuários do sistema")
    void criar() {

        NotificacaoRequestDto dto = NotificacaoFixture.requestDto();
        Notificacao notificacao = NotificacaoFixture.entity();
        NotificacaoResponseDto responseDto = NotificacaoFixture.response();

        when(mapper.toEntity(dto)).thenReturn(notificacao);
        when(repository.save(notificacao)).thenReturn(notificacao);
        when(mapper.toResponse(notificacao)).thenReturn(responseDto);

        NotificacaoResponseDto resposta = service.criar(dto);

        assertNotNull(resposta.id());
        assertEquals(dto.notificacao(), resposta.notificacao());
        assertEquals(responseDto.dataDeCriacao(), resposta.dataDeCriacao());

        verify(usuarioNotificacaoRepository)
                .enviarParaTodosUsuarios(notificacao.getId());
    }

    @Test
    @DisplayName("Deve retornar notificações não lidas do usuário")
    void deveRetornarNotificacoesNaoLidas() {

        Long idUsuario = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadePrincipal(user);

        Notificacao notificacao1 = NotificacaoFixture.entity();
        Notificacao notificacao2 = NotificacaoFixture.entity();

        List<Notificacao> lista = List.of(notificacao1, notificacao2);

        when(usuarioService.getUsuario(idUsuario)).thenReturn(usuario);

        when(usuarioNotificacaoRepository.buscarNotificacoesNaoLidas(idUsuario))
                .thenReturn(lista);


        when(mapper.toResponse(notificacao1))
                .thenReturn(new NotificacaoResponseDto(
                        notificacao1.getId(),
                        notificacao1.getNotificacao(),
                        notificacao1.getDataDeCriacao()
                ));

        when(mapper.toResponse(notificacao2))
                .thenReturn(new NotificacaoResponseDto(
                        notificacao2.getId(),
                        notificacao2.getNotificacao(),
                        notificacao2.getDataDeCriacao()
                ));

        Page<NotificacaoResponseDto> resultado = service.naoLidas(idUsuario, pageable);

        assertEquals(2, resultado.getTotalElements());
        assertEquals(notificacao1.getId(), resultado.getContent().get(0).id());
        assertEquals(notificacao2.getId(), resultado.getContent().get(1).id());

        verify(usuarioService).getUsuario(idUsuario);
        verify(usuarioNotificacaoRepository).buscarNotificacoesNaoLidas(idUsuario);

        // 🔹 garante que cleanup NÃO ocorre no service
        verify(repository, never()).deleteByDataDeCriacaoBefore(any());
        verify(usuarioNotificacaoRepository, never())
                .deleteByNotificacaoDataDeCriacaoBefore(any());
    }

    @Test
    @DisplayName("Deve marcar todas as notificações do usuário como lidas")
    void deveMarcarNotificacoesDoUsuarioComoLidas() {

        Long idUsuario = 1L;

        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        Usuario usuario = UserFixture.entidadeCompleta(user);

        when(usuarioService.getUsuario(idUsuario)).thenReturn(usuario);

        service.marcarComoLidas(idUsuario);

        assertNotNull(usuario.getUser());

        verify(usuarioService).getUsuario(idUsuario);
        verify(usuarioNotificacaoRepository).marcarComoLidas(idUsuario);

        verify(repository, never()).deleteByDataDeCriacaoBefore(any());
        verify(usuarioNotificacaoRepository, never())
                .deleteByNotificacaoDataDeCriacaoBefore(any());
    }
}