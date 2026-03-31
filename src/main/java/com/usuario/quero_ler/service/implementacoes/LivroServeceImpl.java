package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.dtos.livro.BuscaDeLivrosRequest;
import com.usuario.quero_ler.dtos.livro.LivroCardResponse;
import com.usuario.quero_ler.dtos.livro.LivroRequest;
import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.exceptions.especies.*;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.repository.LivroRepository;
import com.usuario.quero_ler.service.AutorServiceI;
import com.usuario.quero_ler.service.LivroServiceI;
import com.usuario.quero_ler.service.LoginServiceI;
import com.usuario.quero_ler.utils.LivroFiltro;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LivroServeceImpl implements LivroServiceI {

    private final LivroRepository repository;
    private final LivroMapper mapper;
    private final AutorServiceI autorServiceI;
    private final LoginServiceI loginServiceI;

    @Override
    public LivroResponse criar(LivroRequest dto,MultipartFile capaDoLivro) {
        loginServiceI.validarLogin();

        Optional<Livro> isbn = repository.findByIsbn(dto.isbn());
        if(isbn.isPresent()){
            throw new IsbnJaCadastradoException("Isbn já cadatrado");
        }

        Livro livro = mapper.toEntity(dto);
        for (AutorRequest autorRequest : dto.autores()){
            Autor autor = autorServiceI.criar(autorRequest);
            livro.adicionarAutor(autor);
        }
        if (capaDoLivro != null && !capaDoLivro.isEmpty()){
            validarCapaDoLivro(capaDoLivro);
            try {
                livro.setCapaDoLivro(capaDoLivro.getBytes());
            } catch (IOException e) {
                throw new CapaForaDePadraoException("Erro ao ler imagem"+ e);
            }
        }

        livro = repository.save(livro);

        return mapper.toResponse(livro);
    }

    @Override
    public Page<LivroCardResponse> listar(Pageable pageable) {
        loginServiceI.validarLogin();
        Page<LivroCardResponse> livros = repository.findAll(pageable).map(mapper::toCardResponse);
        return livros;
    }

    @Override
    public Page<LivroResponse> listarPopulares(){
        Pageable pageable= LivroFiltro.top5MaisVotados();
        Page<LivroResponse> livros = repository.findAll(pageable).map(mapper ::toResponse);
        if(livros.isEmpty()){
            throw new LivroNaoEncontradoException("Não há livros em top5!");
        }
        return livros;
    }

    @Override
    public void inserirCapaDoLivro(Long id, MultipartFile capaDoLivro) {
        loginServiceI.validarLogin();
        validarCapaDoLivro(capaDoLivro);
        Livro livro = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado"));

        try {
            livro.setCapaDoLivro(capaDoLivro.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler imagem", e);
        }

        repository.save(livro);
    }

    @Override
    public byte[] buscarCapa(Long id){
        loginServiceI.validarLogin();
        Livro livro = repository.findById(id).orElseThrow(
                ()-> new LivroNaoEncontradoException("Livro não encontrado")
        );

        if (livro.getCapaDoLivro()== null){
            throw new CapaNaoCadastradaException("Capa não cadastrada");
        } else {
            return livro.getCapaDoLivro();
        }
    }

    @Override
    public LivroResponse buscarIsbn(String isbn){
        loginServiceI.validarLogin();
        Livro livro = livro = repository.findByIsbn(isbn).orElseThrow(
                ()-> new IsbnNaoEncontradoException("Não há nenhum livro cadastrado com o código ISBN informado")
        );
        return mapper.toResponse(livro);
    }

    @Override
    public Page<LivroCardResponse> buscar(String titulo, String editora,String autor, Pageable pageable){
        loginServiceI.validarLogin();
        Specification<Livro> filtro = LivroFiltro.filtro(titulo, editora, autor);
        Page<LivroCardResponse> livros = repository.findAll(filtro,pageable).map(mapper ::toCardResponse);
        if(livros.isEmpty()){
            throw new LivroNaoEncontradoException("Nenhum livro encontrado para essa busca!");
        }
        return livros;
    }

    protected void validarCapaDoLivro(MultipartFile capaDoLivro) {
        try {
            if (capaDoLivro == null || capaDoLivro.isEmpty()) {
                return;
            }

            long tamanhoMaximo = 10 * 1024 * 1024;
            if (capaDoLivro.getSize() > tamanhoMaximo) {
                throw new CapaForaDePadraoException("Imagem excede o tamanho máximo de 10MB");
            }

            List<String> tiposPermitidos = List.of(
                    "image/jpeg",
                    "image/jpg",
                    "image/png"
            );

            if (capaDoLivro.getContentType() == null ||
                    !tiposPermitidos.contains(capaDoLivro.getContentType())) {
                throw new CapaForaDePadraoException("Formato inválido. Use JPG ou PNG");
            }

            BufferedImage imagem = ImageIO.read(capaDoLivro.getInputStream());
            if (imagem == null) {
                throw new CapaForaDePadraoException("Arquivo enviado não é uma imagem válida");
            }

        } catch (IOException e) {
            throw new CapaForaDePadraoException("Erro ao processar imagem");
        }
    }
}