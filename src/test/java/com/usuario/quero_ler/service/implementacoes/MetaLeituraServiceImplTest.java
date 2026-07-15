package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.exceptions.especies.DataInvalidaException;
import com.usuario.quero_ler.exceptions.especies.MetaDeLeituraJaCadastradaException;
import com.usuario.quero_ler.exceptions.especies.MetaDeLeituraNaoEncontradaException;
import com.usuario.quero_ler.fixtures.MetaLeituraFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.MetaLeituraMapper;
import com.usuario.quero_ler.models.MetaLeitura;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.repository.MetaLeituraRepository;
import com.usuario.quero_ler.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetaLeituraServiceImplTest {

    @Mock
    private MetaLeituraRepository repository;

    @Mock
    private LoginService loginService;

    @Mock
    private MetaLeituraMapper mapper;

    @InjectMocks
    private MetaLeituraServiceImpl service;

    @Test
    @DisplayName("Deve Criar nova meta com os dados informados no DTO.")
    void deveCriarNovaMetaComUsuarioLogado() {
        MetaRequestDto dto = MetaLeituraFixture.requestDto(2027);

        Usuario usuario = UserFixture.entidadeCompleta();

        MetaLeitura meta = MetaLeituraFixture.metaLeitura(dto);

        when(loginService.getUsuarioLogado()).thenReturn(usuario.getUser());
        when(mapper.toMetaLeitura(dto)).thenReturn(meta);
        when(repository.save(meta)).thenReturn(meta);

        service.novaMeta(dto);

        ArgumentCaptor<MetaLeitura> captor = ArgumentCaptor.forClass(MetaLeitura.class);

        verify(repository).save(captor.capture());

        MetaLeitura metaSalva = captor.getValue();

        assertEquals(dto.ano(), metaSalva.getAno());
        assertEquals(dto.metaLivrosAno(), metaSalva.getMetaLivrosAno());
        assertEquals(dto.metaLivrosMes(), metaSalva.getMetaLivrosMes());
        assertEquals(dto.metaPaginasDia(), metaSalva.getMetaPaginasDia());

        verify(loginService).getUsuarioLogado();
        verify(mapper).toMetaLeitura(dto);
        verify(repository).save(meta);
    }

    @Test
    @DisplayName("Deve Criar nova meta com os dados informados no DTO, porém o ano corrente.")
    void deveCriarNovaMetaComAnoAtualComUsuarioLogado() {
        MetaRequestDto dto = MetaLeituraFixture.requestDto(null);
        Integer anoCorrente = LocalDate.now().getYear();

        Usuario usuario = UserFixture.entidadeCompleta();

        MetaLeitura meta = MetaLeituraFixture.metaLeitura(dto);

        when(loginService.getUsuarioLogado()).thenReturn(usuario.getUser());
        when(mapper.toMetaLeitura(dto)).thenReturn(meta);
        when(repository.save(meta)).thenReturn(meta);

        service.novaMeta(dto);

        ArgumentCaptor<MetaLeitura> captor = ArgumentCaptor.forClass(MetaLeitura.class);

        verify(repository).save(captor.capture());

        MetaLeitura metaSalva = captor.getValue();

        assertEquals(anoCorrente, metaSalva.getAno());
        assertEquals(dto.metaLivrosAno(), metaSalva.getMetaLivrosAno());
        assertEquals(dto.metaLivrosMes(), metaSalva.getMetaLivrosMes());
        assertEquals(dto.metaPaginasDia(), metaSalva.getMetaPaginasDia());

        verify(loginService).getUsuarioLogado();
        verify(mapper).toMetaLeitura(dto);
        verify(repository).save(meta);
    }

    @Test
    @DisplayName("Deve atualizar meta existente com os dados informados no DTO.")
    void deveAtualizarMetaExistenteComUsuarioLogado() {
        Integer ano = LocalDate.now().getYear();

        MetaRequestDto dto = MetaLeituraFixture.requestDto(ano);
        Usuario usuario = UserFixture.entidadeCompleta();
        MetaLeitura meta = MetaLeituraFixture.metaLeitura(dto);

        when(loginService.getUsuarioLogado()).thenReturn(usuario.getUser());
        when(repository.findByUsuarioAndAno(usuario, ano)).thenReturn(Optional.of(meta));

        service.atualizar(dto);

        verify(loginService).getUsuarioLogado();
        verify(repository).findByUsuarioAndAno(usuario, ano);
        verify(mapper).atualizarMetaLeitura(meta, dto);
        verify(repository).save(meta);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar uma meta inexistente.")
    void deveLancarExcecaoAoTentarAtualizarMetaInexistente() {
        Integer ano = LocalDate.now().getYear();

        MetaRequestDto dto = MetaLeituraFixture.requestDto(ano);
        Usuario usuario = UserFixture.entidadeCompleta();

        when(loginService.getUsuarioLogado()).thenReturn(usuario.getUser());
        when(repository.findByUsuarioAndAno(usuario, ano)).thenReturn(Optional.empty());

        MetaDeLeituraNaoEncontradaException exception = assertThrows(MetaDeLeituraNaoEncontradaException.class,
                () -> service.atualizar(dto));

        assertEquals("Não há meta cadastrada para o ano de: " + ano + ".", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção de data iválida, por tentativa de criar meta para anos preteritos.")
    void deveLancarExcecaoAoTentarCriarNovaMetaParaAnoPreterito() {
        MetaRequestDto dto = MetaLeituraFixture.requestDto(2020);
        Integer anoCorrente = LocalDate.now().getYear();

        Usuario usuario = UserFixture.entidadeCompleta();

        MetaLeitura meta = MetaLeituraFixture.metaLeitura(dto);

        when(loginService.getUsuarioLogado()).thenReturn(usuario.getUser());

        DataInvalidaException exception = assertThrows(DataInvalidaException.class,
                () -> service.novaMeta(dto));

        assertEquals("O ano informado não pode ser anterior ao corrente.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção de meta já cadastrada para esse ano.")
    void deveLancarExcecaoDeMetaJaExistenteParaEsseAno() {
        Integer anoCorrente = LocalDate.now().getYear();
        MetaRequestDto dto = MetaLeituraFixture.requestDto(anoCorrente);

        Usuario usuario = UserFixture.entidadeCompleta();

        MetaLeitura meta = MetaLeituraFixture.metaLeitura(dto);

        when(loginService.getUsuarioLogado()).thenReturn(usuario.getUser());
        when(repository.existsByUsuarioAndAno(usuario, anoCorrente)).thenReturn(true);

        MetaDeLeituraJaCadastradaException exception = assertThrows(MetaDeLeituraJaCadastradaException.class,
                () -> service.novaMeta(dto));

        assertEquals("Já há meta cadastrada para o ano de: " + anoCorrente + ".", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar todas as metas do usuario.")
    void deveDeletarTodasAsMetasDoUsuarioLogado() {
        MetaRequestDto dto = MetaLeituraFixture.requestDto(2027);
        Usuario usuario = UserFixture.entidadeCompleta();

        when(loginService.getUsuarioLogado()).thenReturn(usuario.getUser());

        service.deletar();

        verify(loginService).getUsuarioLogado();
        verify(repository).deleteAllByUsuario(usuario);
        verifyNoMoreInteractions(repository);
    }
}