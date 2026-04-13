package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.livro.LivroCardResponse;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enuns.LivroStatus;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.CapaForaDePadraoException;
import com.usuario.quero_ler.exceptions.especies.IsbnNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.LivroNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioJaPossueOLivroException;
import com.usuario.quero_ler.fixtures.LivroFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.*;
import com.usuario.quero_ler.repository.LivroRepository;
import com.usuario.quero_ler.utils.LivroFiltro;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServeceImplTest {

    @InjectMocks
    private LivroServeceImpl service;

    @Mock
    private LivroRepository repository;

    @Mock
    private LivroMapper mapper;

    @Mock
    private AutorServiceImpl autorService;

    @Mock
    private MultipartFile multipartFile;

    private User leitor;
    private User admin;

    @BeforeEach
    void setUp() {
        leitor = UserFixture.userEntity(UsuarioProfile.LEITOR);
        admin = UserFixture.userEntity(UsuarioProfile.ADMINISTRADOR);
    }

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    void deveCriarUmLivroComSucesso() {
        LivroRequest dto = LivroFixture.request();
        Livro livro = LivroFixture.entity();
        LivroResponse response = LivroFixture.response();

        when(mapper.toEntity(dto)).thenReturn(livro);
        when(autorService.criar(dto.autores().get(0))).thenReturn(livro.getAutores().get(0));

        when(repository.save(livro)).thenReturn(livro);
        when(mapper.toResponse(livro)).thenReturn(response);

        LivroResponse resultado = service.criar(dto, null);

        assertNotNull(resultado.id());
        assertEquals(dto.titulo(), resultado.titulo());
        assertEquals(dto.isbn(), resultado.isbn());
        assertEquals(dto.editora(), resultado.editora());
        assertEquals(dto.anoDePublicacao(), resultado.anoDePublicacao());
        assertEquals(dto.numeroDePaginas(), resultado.numeroDePaginas());
        assertEquals(dto.idioma(), resultado.idioma());
        assertEquals(dto.sinopse(), resultado.sinopse());
        assertEquals("/livros/" + resultado.id() + "/capa", resultado.capaUrl());

        verify(mapper).toEntity(dto);
        verify(repository).save(livro);
    }


    @Test
    @DisplayName("Deve retornar uma lista de livro com sucesso.")
    void deveRetornarUmalistaDeLivros() {

        Livro livro = LivroFixture.entity();

        Pageable pageable = PageRequest.of(0, 10);

        LivroCardResponse response = LivroFixture.responseCard();
        Page<Livro> page = new PageImpl<>(List.of(livro));
        Specification<Livro> filtro = LivroFiltro.filtro(null, null, null);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toCardResponse(livro)).thenReturn(response);

        Page<LivroCardResponse> resultado = service.buscar(null, null, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(livro.getAutores().get(0).getNome(), resultado.getContent().get(0).autores().get(0).nome());

        verify(mapper).toCardResponse(livro);
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Deve buscar um livro por ISBN com sucesso.")
    void deveBuscarPorIsbn() {
        Livro livro = LivroFixture.entity();
        String isbn = livro.getIsbn();
        LivroResponse response = LivroFixture.response();

        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(livro));
        when(mapper.toResponse(livro)).thenReturn(response);

        LivroResponse resposta = service.buscarIsbn(isbn);

        assertNotNull(resposta.id());
        assertEquals(livro.getTitulo(), resposta.titulo());
        assertEquals(livro.getIsbn(), resposta.isbn());
        assertEquals(livro.getEditora(), resposta.editora());
        assertEquals(livro.getAnoDePublicacao(), resposta.anoDePublicacao());
        assertEquals(livro.getNumeroDePaginas(), resposta.numeroDePaginas());
        assertEquals(livro.getIdioma(), resposta.idioma());
        assertEquals(livro.getSinopse(), resposta.sinopse());

        verify(repository).findByIsbn(isbn);
        verify(mapper).toResponse(livro);
    }

    @Test
    @DisplayName("Deve buscar um livro por autor com sucesso.")
    void deveBuscarPorAutor() {

        Livro livro = LivroFixture.entity();
        String autorNome = livro.getAutores().get(0).getNome();

        Pageable pageable = PageRequest.of(0, 10);

        LivroCardResponse response = LivroFixture.responseCard();
        Page<Livro> page = new PageImpl<>(List.of(livro));
        Specification<Livro> filtro = LivroFiltro.filtro(null, null, autorNome);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toCardResponse(livro)).thenReturn(response);

        Page<LivroCardResponse> resultado = service.buscar(null, null, autorNome, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(autorNome, resultado.getContent().get(0).autores().get(0).nome());

        verify(mapper).toCardResponse(livro);
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    @DisplayName("Deve buscar um livro por TITULO com sucesso.")
    void deveBuscarPorTitulo() {

        Livro livro = LivroFixture.entity();
        String titulo = livro.getTitulo();

        Pageable pageable = PageRequest.of(0, 10);

        LivroCardResponse response = LivroFixture.responseCard();
        Page<Livro> page = new PageImpl<>(List.of(livro));
        Specification<Livro> filtro = LivroFiltro.filtro(titulo, null, null);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toCardResponse(livro)).thenReturn(response);

        Page<LivroCardResponse> resultado = service.buscar(titulo, null, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(titulo, resultado.getContent().get(0).titulo());

        verify(mapper).toCardResponse(livro);
        verify(repository).findAll(any(Specification.class), eq(pageable));

    }

    @Test
    @DisplayName("Deve buscar um livro por EDITORA com sucesso.")
    void deveBuscarPorEditora() {

        Livro livro = LivroFixture.entity();
        String editora = livro.getEditora();

        Pageable pageable = PageRequest.of(0, 10);

        LivroCardResponse response = LivroFixture.responseCard();
        Page<Livro> page = new PageImpl<>(List.of(livro));
        Specification<Livro> filtro = LivroFiltro.filtro(null, editora, null);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toCardResponse(livro)).thenReturn(response);

        Page<LivroCardResponse> resultado = service.buscar(null, editora, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(editora, resultado.getContent().get(0).editora());

        verify(mapper).toCardResponse(livro);
        verify(repository).findAll(any(Specification.class), eq(pageable));

    }

    @Test
    @DisplayName("Deve inserir capa do livro com sucesso")
    void deveInserirCapaDoLivroComSucesso() throws Exception {
        Livro livro = LivroFixture.entity();
        Long id = livro.getId();

        byte[] imagem = LivroFixture.entityComCapa().getCapaDoLivro();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "capa.jpg",
                "image/jpeg",
                imagem
        );

        when(repository.findById(id)).thenReturn(Optional.of(livro));

        service.inserirCapaDoLivro(id, file);

        assertArrayEquals(imagem, livro.getCapaDoLivro());
        verify(repository).save(livro);
    }

    @Test
    @DisplayName("Deve buscar capa do livro com sucesso")
    void deveBuscarCapaDoLivroComSucesso() throws Exception {
        Livro livro = LivroFixture.entityComCapa();
        Long id = livro.getId();

        when(repository.findById(id)).thenReturn(Optional.of(livro));

        byte[] resposta = service.buscarCapa(id);

        assertArrayEquals(livro.getCapaDoLivro(), resposta);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Deve buscar um livro pelo ISBN com sucesso")
    void deveBuscarUmLivroPorIsbn() {
        Livro livro = LivroFixture.entity();
        String isbn = livro.getIsbn();
        LivroResponse response = LivroFixture.response();

        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(livro));
        when(mapper.toResponse(livro)).thenReturn(response);

        LivroResponse resposta = service.buscarIsbn(isbn);

        assertNotNull(resposta.id());
        assertEquals(livro.getTitulo(), resposta.titulo());
        assertEquals(livro.getIsbn(), resposta.isbn());
        assertEquals(livro.getEditora(), resposta.editora());
        assertEquals(livro.getAnoDePublicacao(), resposta.anoDePublicacao());
        assertEquals(livro.getNumeroDePaginas(), resposta.numeroDePaginas());
        assertEquals(livro.getIdioma(), resposta.idioma());
        assertEquals(livro.getSinopse(), resposta.sinopse());

        verify(repository).findByIsbn(isbn);
        verify(mapper).toResponse(livro);
    }

    @Test
    @DisplayName("Deve lançar excessão ao buscar um livro pelo ISBN inexistente")
    void deveLancarExcessaoAoBuscarUmLivroPorIsbnInexixtente() {
        Livro livro = LivroFixture.entity();
        String isbn = livro.getIsbn();

        when(repository.findByIsbn(isbn)).thenReturn(Optional.empty());

        IsbnNaoEncontradoException exception = assertThrows(IsbnNaoEncontradoException.class,
                () -> service.buscarIsbn(isbn)
        );

        assertEquals("Não há nenhum livro cadastrado com o código ISBN informado",
                exception.getMessage());

        verify(repository).findByIsbn(isbn);
    }

    @Test
    @DisplayName("Deve validar uma capa de livro com sucesso")
    void deveValidarImagemValida() {
        byte[] imagem = LivroFixture.entityComCapa().getCapaDoLivro();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "capa.jpg",
                "image/jpeg",
                imagem
        );

        assertDoesNotThrow(() -> service.validarCapaDoLivro(file));
    }

    @Test
    @DisplayName("Deve ignorar uma capa de livro vazia.")
    void deveValidarImagemVazia() {
        byte[] imagem = LivroFixture.entityComCapa().getCapaDoLivro();
        MockMultipartFile file = null;

        assertDoesNotThrow(() -> service.validarCapaDoLivro(file));
    }

    @Test
    @DisplayName("Deve lançar exception para imagem da capa de livro com mais de 10MB.")
    void deveLacarExcessaoDeImagemAcimaDe10MB() {
        byte[] imagem = new byte[11 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "capa.jpg",
                "image/jpeg",
                imagem
        );

        CapaForaDePadraoException exception = assertThrows(CapaForaDePadraoException.class,
                () -> service.validarCapaDoLivro(file)
        );

        assertEquals("Imagem excede o tamanho máximo de 10MB", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exception para imagem da capa de livro com formato não permitido.")
    void deveLancarExcecaoParaFormatoInvalido() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "capa.txt",
                "text/plain",
                "qualquer coisa".getBytes()
        );

        CapaForaDePadraoException exception = assertThrows(CapaForaDePadraoException.class,
                () -> service.validarCapaDoLivro(file));

        assertEquals("Formato inválido. Use JPG ou PNG", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exception para imagem da capa de livro for inválida")
    void deveLancarExcecaoQuandoNaoForImagemValida() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "capa.jpg",
                "image/jpeg",
                "isso nao é imagem".getBytes()
        );

        CapaForaDePadraoException exception = assertThrows(CapaForaDePadraoException.class,
                () -> service.validarCapaDoLivro(file));
        assertEquals("Arquivo enviado não é uma imagem válida", exception.getMessage());

    }

//
//    @Test
//    @DisplayName("Deve adicionar um livro a estante de um usuário")
//    void deveAdicionarLivroNaEstanteDoUsuario() {
//        Usuario usuario = UserFixture.entidadeCompleta();
//        Long idUsuario = usuario.getId();
//        Livro livro = LivroFixture.entity();
//        Long idDoLivro = livro.getId();
//        UsuarioLivroId usuarioLivroId = new UsuarioLivroId();
//        usuarioLivroId.setLivroId(idDoLivro);
//        usuarioLivroId.setUsuarioId(usuario.getId());
//        UsuarioLivro usuarioLivro = new UsuarioLivro(usuarioLivroId, LivroStatus.LIVROS_QUE_QUERO_LER, usuario, livro);
//
//        when(service.buscar(idDoLivro)).thenReturn(livro);
//        when(usuarioServiceI.getUsuario(idUsuario)).thenReturn(usuario);
//        when(repository.findByUsuarioIdAndLivroId(idUsuario, idDoLivro)).thenReturn(Optional.empty());
//
//        service.adicionar(idUsuario, idDoLivro);
//        ArgumentCaptor<UsuarioLivro> captor = ArgumentCaptor.forClass(UsuarioLivro.class);
//        verify(repository).save(captor.capture());
//
//        UsuarioLivro salvo = captor.getValue();
//
//        assertEquals(idUsuario, salvo.getUsuario().getId());
//        assertEquals(idDoLivro, salvo.getLivro().getId());
//        assertEquals(LivroStatus.LIVROS_QUE_QUERO_LER, salvo.getStatus());
//
//        verify(repository).findByUsuarioIdAndLivroId(idUsuario, idDoLivro);
//        verify(usuarioServiceI).getUsuario(idUsuario);
//        verify(livroServiceI).buscar(idDoLivro);
//    }
//
//    @Test
//    @DisplayName("Deve lançar excessão ao tentar adicionar um livro já existente na estante de um usuário")
//    void deveLancarExcessaoAoTentarAdicionarLivroNaEstanteDoUsuario() {
//        Usuario usuario = UserFixture.entidadeCompleta();
//        Long idUsuario = usuario.getId();
//        Livro livro = LivroFixture.entity();
//        Long idDoLivro = livro.getId();
//        UsuarioLivroId usuarioLivroId = new UsuarioLivroId();
//        usuarioLivroId.setLivroId(idDoLivro);
//        usuarioLivroId.setUsuarioId(usuario.getId());
//        UsuarioLivro usuarioLivro = new UsuarioLivro(usuarioLivroId, LivroStatus.LIVROS_QUE_QUERO_LER, usuario, livro);
//
//        when(livroServiceI.buscar(idDoLivro)).thenReturn(livro);
//        when(usuarioServiceI.getUsuario(idUsuario)).thenReturn(usuario);
//        when(repository.findByUsuarioIdAndLivroId(idUsuario, idDoLivro)).thenReturn(Optional.of(usuarioLivro));
//
//        UsuarioJaPossueOLivroException exception = assertThrows(UsuarioJaPossueOLivroException.class,
//                () -> service.adicionar(idUsuario, idDoLivro));
//
//        assertEquals("O usuario já possue o livro na estante.", exception.getMessage());
//
//    }
//
//    @Test
//    @DisplayName("Deve retornar uma lista com todos os livros da estante do usuário")
//    void deveRetornarListaDeLivroDaEstanteDoUsuario() {
//        Long idUsuario = 1L;
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Livro livro1 = LivroFixture.entity();
//        Livro livro2 = LivroFixture.entity();
//
//        UsuarioLivro ul1 = new UsuarioLivro();
//        ul1.setLivro(livro1);
//        ul1.setStatus(LivroStatus.LIVROS_QUE_QUERO_LER);
//
//        UsuarioLivro ul2 = new UsuarioLivro();
//        ul2.setLivro(livro2);
//        ul2.setStatus(LivroStatus.LIVROS_QUE_ESTOU_LENDO);
//
//        List<UsuarioLivro> lista = List.of(ul1, ul2);
//        Page<UsuarioLivro> pageMock = new PageImpl<>(lista, pageable, lista.size());
//
//        LivroTelaLeituraResponse response1 = LivroFixture.responseTelaDeLeitura(LivroStatus.LIVROS_QUE_QUERO_LER);
//        LivroTelaLeituraResponse response2 = LivroFixture.responseTelaDeLeitura(LivroStatus.LIVROS_QUE_ESTOU_LENDO);
//
//        when(repository.findAllByUsuarioId(idUsuario, pageable)).thenReturn(pageMock);
//        when(livroMapper.toLivroTelaLeituraResponse(livro1, LivroStatus.LIVROS_QUE_QUERO_LER)).thenReturn(response1);
//        when(livroMapper.toLivroTelaLeituraResponse(livro2, LivroStatus.LIVROS_QUE_ESTOU_LENDO)).thenReturn(response2);
//
//        Page<LivroTelaLeituraResponse> resultado = service.lista(idUsuario, pageable);
//
//        assertEquals(2, resultado.getContent().size());
//        assertEquals(2, resultado.getTotalElements());
//        assertEquals(response1, resultado.getContent().get(0));
//        assertEquals(response2, resultado.getContent().get(1));
//
//        verify(repository).findAllByUsuarioId(idUsuario, pageable);
//        verify(livroMapper).toLivroTelaLeituraResponse(livro1, LivroStatus.LIVROS_QUE_QUERO_LER);
//        verify(livroMapper).toLivroTelaLeituraResponse(livro2, LivroStatus.LIVROS_QUE_ESTOU_LENDO);
//
//    }
//
//    @Test
//    @DisplayName("Deve mudar o status do livro na estante do usuário")
//    void deveMudarStatusDoLivro() {
//
//        Long idUsuario = 1L;
//        String isbn = "123456";
//        LivroStatus novoStatus = LivroStatus.LIVROS_QUE_QUERO_LER;
//
//        UsuarioLivro usuarioLivro = new UsuarioLivro();
//        usuarioLivro.setStatus(LivroStatus.LIVROS_QUE_QUERO_LER);
//
//        when(repository.findByUsuario_IdAndLivro_Isbn(idUsuario, isbn))
//                .thenReturn(Optional.of(usuarioLivro));
//
//        service.mudarStatus(idUsuario, isbn, novoStatus);
//
//        assertEquals(novoStatus, usuarioLivro.getStatus());
//
//        verify(repository).save(usuarioLivro);
//        verify(repository).findByUsuario_IdAndLivro_Isbn(idUsuario, isbn);
//    }
//
//    @Test
//    @DisplayName("Deve lançar excessão ao tentar mudar o status do livro que não esta a estante do usuário")
//    void deveLancarExcessaoDeLivroInexistente() {
//
//        Long idUsuario = 1L;
//        String isbn = "123456";
//        LivroStatus novoStatus = LivroStatus.LIVROS_QUE_QUERO_LER;
//
//        UsuarioLivro usuarioLivro = new UsuarioLivro();
//        usuarioLivro.setStatus(LivroStatus.LIVROS_QUE_QUERO_LER);
//
//        when(repository.findByUsuario_IdAndLivro_Isbn(idUsuario, isbn))
//                .thenReturn(Optional.empty());
//
//        LivroNaoEncontradoException exception = assertThrows(LivroNaoEncontradoException.class,
//                () -> service.mudarStatus(idUsuario, isbn, novoStatus));
//
//        assertEquals("O usuario não possue o livro na estante.", exception.getMessage());
//    }
}
