package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.enuns.DocumentoTipo;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.UsuarioSemPermissaoParaAcaoException;
import com.usuario.quero_ler.fixtures.DocumentoFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.DocumentoMapper;
import com.usuario.quero_ler.models.Documento;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.repository.DocumentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentoServiceImplTest {

    @InjectMocks
    private DocumentoServiceImpl service;

    @Mock
    private DocumentoRepository repository;

    @Mock
    private LoginServiceImpl loginService;

    @Mock
    private NotificacaoServiceImpl notificacaoService;

    @Mock
    private DocumentoMapper mapper;

    @Test
    @DisplayName("Deve criar um documento com sucesso.")
    void deveCriarUmNovoDocumentoComSucesso() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        DocumentoRequestDto dto = DocumentoFixture.requestDto();
        Documento documento = DocumentoFixture.entity();
        DocumentoResponseDto responseDto = DocumentoFixture.responseDto();

        when(loginService.validarLogin()).thenReturn(user);
        when(mapper.toEntity(dto)).thenReturn(documento);
        when(repository.save(documento)).thenReturn(documento);
        when(mapper.toResponse(documento)).thenReturn(responseDto);

        DocumentoResponseDto resposta = service.criar(dto);

        assertNotNull(resposta.id());
        assertEquals(dto.titulo(), resposta.titulo());
        assertEquals(dto.tipo(), resposta.tipo());
        assertEquals(dto.conteudo(), resposta.conteudo());

        verify(repository).save(documento);
        verify(notificacaoService).criar(new NotificacaoRequestDto("Documento criado"));
    }

    @Test
    @DisplayName("Deve lançar excessão auando um usuario sem perfil de administrador tentar criar um documento.")
    void deveLancarExcessaoQuandoUmUsuarioSemPerfilDeAdministradorTentaCriarUmNovoDocumento() {
        User user = UserFixture.userEntity(UsuarioProfile.LEITOR);
        DocumentoRequestDto dto = DocumentoFixture.requestDto();

        when(loginService.validarLogin()).thenReturn(user);

        UsuarioSemPermissaoParaAcaoException exception = assertThrows(UsuarioSemPermissaoParaAcaoException.class,
                () -> service.criar(dto)
        );

        assertEquals("Apenas administradores podem executar esta ação.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve alterar um documento com sucesso;")
    void deveAlterarUmDocumentoComSucesso() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        Documento documento = DocumentoFixture.entity();
        LocalDateTime agora = LocalDateTime.now();
        Long id = documento.getId();
        DocumentoAlteracoesDto alteracoes = new DocumentoAlteracoesDto("Titulo alterado", null, "conteudo alterado");
        Documento documentoAlterado = new Documento(id, alteracoes.titulo(), documento.getTipo(), alteracoes.conteudo(), agora);

        when(loginService.validarLogin()).thenReturn(user);
        when(repository.findById(id)).thenReturn(Optional.of(documento));
        when(mapper.toUpdate(documento, alteracoes)).thenReturn(documentoAlterado);

        service.alterar(id, alteracoes);

        assertEquals(alteracoes.titulo(), documentoAlterado.getTitulo());
        verify(repository).save(documentoAlterado);
        verify(notificacaoService).criar(new NotificacaoRequestDto("documento alterado"));
    }

    @Test
    @DisplayName("Deve retornar o Termos Gerais de uso")
    void deveRetornarOTermosGeraisDeUso() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
        Documento documento = DocumentoFixture.entity();
        DocumentoResponseDto reponse = DocumentoFixture.responseDto(documento);

        when(loginService.validarLogin()).thenReturn(user);
        when(repository.findTopByTipoOrderByUltimaAlteracaoDesc(DocumentoTipo.TERMOS_GERAIS_DE_USO)).thenReturn(documento);
        when(mapper.toResponse(documento)).thenReturn(reponse);

        DocumentoResponseDto resposta = service.getTermosGeraisDeUso();

        assertEquals(DocumentoTipo.TERMOS_GERAIS_DE_USO, resposta.tipo());
        verify(repository).findTopByTipoOrderByUltimaAlteracaoDesc(DocumentoTipo.TERMOS_GERAIS_DE_USO);
    }

    @Test
    @DisplayName("Deve gerar notificações para todos usuarios")
    void gerarNonificacao() {
        String mensagem = "Testando as notificações";
        NotificacaoRequestDto notificacaoRequestDto = new NotificacaoRequestDto(mensagem);

        service.gerarNonificacao(mensagem);

        verify(notificacaoService).criar(notificacaoRequestDto);
    }

    @Test
    @DisplayName("Deve validar usuário logado como administrador.")
    void deveValidarUsuarioComoAdm() {
        User user = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);

        when(loginService.validarLogin()).thenReturn(user);

        service.validarUsuario();

        verify(loginService).validarLogin();
    }
}