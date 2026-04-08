package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enuns.LivroStatus;
import com.usuario.quero_ler.exceptions.especies.LivroNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioJaPossueOLivroException;
import com.usuario.quero_ler.fixtures.LivroFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.Usuario;
import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.models.UsuarioLivroId;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import com.usuario.quero_ler.service.LivroServiceI;
import com.usuario.quero_ler.service.UsuarioServiceI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstanteDoUsuarioServiceImplTest {

    @InjectMocks
    private EstanteDoUsuarioServiceImpl service;

    @Mock
    private LivroServiceI livroServiceI;
    @Mock
    private UsuarioServiceI usuarioServiceI;
    @Mock
    private UsuarioLivroRepository repository;
    @Mock
    private LivroMapper livroMapper;


    @Test
    @DisplayName("Deve adicionar um livro a estante de um usuário")
    void deveAdicionarLivroNaEstanteDoUsuario() {
        Usuario usuario = UserFixture.entidadeCompleta();
        Long idUsuario = usuario.getId();
        Livro livro = LivroFixture.entity();
        Long idDoLivro = livro.getId();
        UsuarioLivroId usuarioLivroId = new UsuarioLivroId();
        usuarioLivroId.setLivroId(idDoLivro);
        usuarioLivroId.setUsuarioId(usuario.getId());
        UsuarioLivro usuarioLivro = new UsuarioLivro(usuarioLivroId, LivroStatus.LIVROS_QUE_QUERO_LER, usuario, livro);

        when(livroServiceI.buscar(idDoLivro)).thenReturn(livro);
        when(usuarioServiceI.getUsuario(idUsuario)).thenReturn(usuario);
        when(repository.findByUsuarioIdAndLivroId(idUsuario, idDoLivro)).thenReturn(Optional.empty());

        service.adicionar(idUsuario, idDoLivro);
        ArgumentCaptor<UsuarioLivro> captor = ArgumentCaptor.forClass(UsuarioLivro.class);
        verify(repository).save(captor.capture());

        UsuarioLivro salvo = captor.getValue();

        assertEquals(idUsuario, salvo.getUsuario().getId());
        assertEquals(idDoLivro, salvo.getLivro().getId());
        assertEquals(LivroStatus.LIVROS_QUE_QUERO_LER, salvo.getStatus());

        verify(repository).findByUsuarioIdAndLivroId(idUsuario, idDoLivro);
        verify(usuarioServiceI).getUsuario(idUsuario);
        verify(livroServiceI).buscar(idDoLivro);
    }

    @Test
    @DisplayName("Deve lançar excessão ao tentar adicionar um livro já existente na estante de um usuário")
    void deveLancarExcessaoAoTentarAdicionarLivroNaEstanteDoUsuario() {
        Usuario usuario = UserFixture.entidadeCompleta();
        Long idUsuario = usuario.getId();
        Livro livro = LivroFixture.entity();
        Long idDoLivro = livro.getId();
        UsuarioLivroId usuarioLivroId = new UsuarioLivroId();
        usuarioLivroId.setLivroId(idDoLivro);
        usuarioLivroId.setUsuarioId(usuario.getId());
        UsuarioLivro usuarioLivro = new UsuarioLivro(usuarioLivroId, LivroStatus.LIVROS_QUE_QUERO_LER, usuario, livro);

        when(livroServiceI.buscar(idDoLivro)).thenReturn(livro);
        when(usuarioServiceI.getUsuario(idUsuario)).thenReturn(usuario);
        when(repository.findByUsuarioIdAndLivroId(idUsuario, idDoLivro)).thenReturn(Optional.of(usuarioLivro));

        UsuarioJaPossueOLivroException exception = assertThrows(UsuarioJaPossueOLivroException.class,
                () -> service.adicionar(idUsuario, idDoLivro));

        assertEquals("O usuario já possue o livro na estante.", exception.getMessage());

    }

    @Test
    @DisplayName("Deve retornar uma lista com todos os livros da estante do usuário")
    void deveRetornarListaDeLivroDaEstanteDoUsuario() {
        Long idUsuario = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Livro livro1 = LivroFixture.entity();
        Livro livro2 = LivroFixture.entity();

        UsuarioLivro ul1 = new UsuarioLivro();
        ul1.setLivro(livro1);
        ul1.setStatus(LivroStatus.LIVROS_QUE_QUERO_LER);

        UsuarioLivro ul2 = new UsuarioLivro();
        ul2.setLivro(livro2);
        ul2.setStatus(LivroStatus.LIVROS_QUE_ESTOU_LENDO);

        List<UsuarioLivro> lista = List.of(ul1, ul2);
        Page<UsuarioLivro> pageMock = new PageImpl<>(lista, pageable, lista.size());

        LivroTelaLeituraResponse response1 = LivroFixture.responseTelaDeLeitura(LivroStatus.LIVROS_QUE_QUERO_LER);
        LivroTelaLeituraResponse response2 = LivroFixture.responseTelaDeLeitura(LivroStatus.LIVROS_QUE_ESTOU_LENDO);

        when(repository.findAllByUsuarioId(idUsuario, pageable)).thenReturn(pageMock);
        when(livroMapper.toLivroTelaLeituraResponse(livro1, LivroStatus.LIVROS_QUE_QUERO_LER)).thenReturn(response1);
        when(livroMapper.toLivroTelaLeituraResponse(livro2, LivroStatus.LIVROS_QUE_ESTOU_LENDO)).thenReturn(response2);

        Page<LivroTelaLeituraResponse> resultado = service.lista(idUsuario, pageable);

        assertEquals(2, resultado.getContent().size());
        assertEquals(2, resultado.getTotalElements());
        assertEquals(response1, resultado.getContent().get(0));
        assertEquals(response2, resultado.getContent().get(1));

        verify(repository).findAllByUsuarioId(idUsuario, pageable);
        verify(livroMapper).toLivroTelaLeituraResponse(livro1, LivroStatus.LIVROS_QUE_QUERO_LER);
        verify(livroMapper).toLivroTelaLeituraResponse(livro2, LivroStatus.LIVROS_QUE_ESTOU_LENDO);

    }

    @Test
    @DisplayName("Deve mudar o status do livro na estante do usuário")
    void deveMudarStatusDoLivro() {

        Long idUsuario = 1L;
        String isbn = "123456";
        LivroStatus novoStatus = LivroStatus.LIVROS_QUE_QUERO_LER;

        UsuarioLivro usuarioLivro = new UsuarioLivro();
        usuarioLivro.setStatus(LivroStatus.LIVROS_QUE_QUERO_LER);

        when(repository.findByUsuario_IdAndLivro_Isbn(idUsuario, isbn))
                .thenReturn(Optional.of(usuarioLivro));

        service.mudarStatus(idUsuario, isbn, novoStatus);

        assertEquals(novoStatus, usuarioLivro.getStatus());

        verify(repository).save(usuarioLivro);
        verify(repository).findByUsuario_IdAndLivro_Isbn(idUsuario, isbn);
    }

    @Test
    @DisplayName("Deve lançar excessão ao tentar mudar o status do livro que não esta a estante do usuário")
    void deveLancarExcessaoDeLivroInexistente() {

        Long idUsuario = 1L;
        String isbn = "123456";
        LivroStatus novoStatus = LivroStatus.LIVROS_QUE_QUERO_LER;

        UsuarioLivro usuarioLivro = new UsuarioLivro();
        usuarioLivro.setStatus(LivroStatus.LIVROS_QUE_QUERO_LER);

        when(repository.findByUsuario_IdAndLivro_Isbn(idUsuario, isbn))
                .thenReturn(Optional.empty());

        LivroNaoEncontradoException exception = assertThrows(LivroNaoEncontradoException.class,
                () -> service.mudarStatus(idUsuario, isbn, novoStatus));

        assertEquals("O usuario não possue o livro na estante.", exception.getMessage());
    }
}