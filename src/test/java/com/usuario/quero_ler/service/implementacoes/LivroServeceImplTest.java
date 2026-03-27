package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.exceptions.especies.CapaForaDePadraoException;
import com.usuario.quero_ler.exceptions.especies.IsbnNaoEncontradoException;
import com.usuario.quero_ler.fixtures.LivroFixture;
import com.usuario.quero_ler.fixtures.UserFixture;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.User;
import com.usuario.quero_ler.repository.LivroRepository;
import com.usuario.quero_ler.utils.LivroFiltro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private LoginServiceImpl login;

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

        when(login.validarLogin()).thenReturn(leitor);
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
        Pageable pageable = PageRequest.of(0, 10);
        Livro livro01 = LivroFixture.entity();
        Livro livro02 = LivroFixture.entity();
        LivroResponse response = LivroFixture.response();
        List<Livro> livros = List.of(livro01, livro02);
        Page<Livro> pageLivros = new PageImpl<>(livros, pageable, livros.size());

        when(login.validarLogin()).thenReturn(leitor);
        when(repository.findAll(pageable)).thenReturn(pageLivros);
        when(mapper.toResponse(livro01)).thenReturn(response);

        Page<LivroResponse> resultado = service.listar(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());

        verify(login).validarLogin();
        verify(repository).findAll(pageable);
        verify(mapper).toResponse(livro01);
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

        LivroResponse response = LivroFixture.response();
        Page<Livro> page = new PageImpl<>(List.of(livro));
        Specification<Livro> filtro = LivroFiltro.filtro(null, null, autorNome);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toResponse(livro)).thenReturn(response);
        when(login.validarLogin()).thenReturn(null); // não esquece

        Page<LivroResponse> resultado = service.buscar(null, null, autorNome, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(autorNome, resultado.getContent().get(0).autores().get(0).nome());

        verify(mapper).toResponse(livro);
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }


    @Test
    @DisplayName("Deve buscar um livro por TITULO com sucesso.")
    void deveBuscarPorTitulo() {

        Livro livro = LivroFixture.entity();
        String titulo = livro.getTitulo();

        Pageable pageable = PageRequest.of(0, 10);

        LivroResponse response = LivroFixture.response();
        Page<Livro> page = new PageImpl<>(List.of(livro));
        Specification<Livro> filtro = LivroFiltro.filtro(titulo, null, null);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toResponse(livro)).thenReturn(response);
        when(login.validarLogin()).thenReturn(null);

        Page<LivroResponse> resultado = service.buscar(titulo, null, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(titulo, resultado.getContent().get(0).titulo());

        verify(mapper).toResponse(livro);
        verify(repository).findAll(any(Specification.class), eq(pageable));

    }

    @Test
    @DisplayName("Deve buscar um livro por EDITORA com sucesso.")
    void deveBuscarPorEditora() {

        Livro livro = LivroFixture.entity();
        String editora = livro.getEditora();

        Pageable pageable = PageRequest.of(0, 10);

        LivroResponse response = LivroFixture.response();
        Page<Livro> page = new PageImpl<>(List.of(livro));
        Specification<Livro> filtro = LivroFiltro.filtro(null, editora, null);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(mapper.toResponse(livro)).thenReturn(response);
        when(login.validarLogin()).thenReturn(null);

        Page<LivroResponse> resultado = service.buscar(null, editora, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(editora, resultado.getContent().get(0).editora());

        verify(mapper).toResponse(livro);
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
}