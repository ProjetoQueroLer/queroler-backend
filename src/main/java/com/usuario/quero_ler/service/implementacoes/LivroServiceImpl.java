package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.autor.AutorRequest;
import com.usuario.quero_ler.dtos.livro.*;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.exceptions.especies.*;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.Autor;
import com.usuario.quero_ler.models.Livro;
import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.repository.LivroRepository;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import com.usuario.quero_ler.service.AutorService;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.LoginService;
import com.usuario.quero_ler.utils.LivroFiltro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LivroServiceImpl implements LivroService {

    private final LivroRepository repository;
    private final LivroMapper mapper;
    private final AutorService autorService;
    private final UsuarioLivroRepository usuarioLivroRepository;
    private final LoginService loginService;

    @Override
    public LivroResponse criar(LivroRequest dto, MultipartFile capaDoLivro) {
        log.info("LivroServiceImpl.criar - titulo={} isbn={}", dto.titulo(), dto.isbn());

        Optional<Livro> isbn = repository.findByIsbn(dto.isbn());
        if (isbn.isPresent()) {
            throw new IsbnJaCadastradoException("Isbn já cadatrado");
        }

        Livro livro = mapper.toEntity(dto);
        for (AutorRequest autorRequest : dto.autores()) {
            Autor autor = autorService.criar(autorRequest);
            livro.adicionarAutor(autor);
        }
        if (capaDoLivro != null && !capaDoLivro.isEmpty()) {
            validarCapaDoLivro(capaDoLivro);
            try {
                livro.setCapaDoLivro(capaDoLivro.getBytes());
            } catch (IOException e) {
                log.error("Erro ao ler imagem", e);
                throw new LerImagemException("Erro ao ler imagem", e);
            }
        }

        livro = repository.save(livro);
        log.info("Livro salvo id={}", livro.getId());
        return mapper.toResponse(livro);
    }

    @Override
    public Page<LivroCardResponse> listar(Pageable pageable) {
        log.info("LivroServiceImpl.listar - iniciando pagina={} size={}", pageable.getPageNumber(),
                pageable.getPageSize());
        Page<LivroCardResponse> livros = repository.findAll(pageable).map(mapper::toCardResponse);
        log.info("LivroServiceImpl.listar - concluído count={}", livros.getTotalElements());
        return livros;
    }

    @Override
    public Page<LivroResponse> listarPopulares() {
        Pageable pageable = LivroFiltro.top5MaisVotados();
        log.info("LivroServiceImpl.listarPopulares");
        Page<LivroResponse> livros = repository.findAll(pageable).map(mapper::toResponse);
        if (livros.isEmpty()) {
            throw new LivroNaoEncontradoException("Não há livros em top5!");
        }
        return livros;
    }

    @Override
    public void inserirCapaDoLivro(Long id, MultipartFile capaDoLivro) {
        log.info("LivroServiceImpl.inserirCapaDoLivro - iniciando id={}", id);
        validarCapaDoLivro(capaDoLivro);
        Livro livro = repository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException("Livro não encontrado"));

        try {
            livro.setCapaDoLivro(capaDoLivro.getBytes());
        } catch (IOException e) {
            log.error("Erro ao ler capa do livro id={}", id, e);
            throw new LerImagemException("Erro ao ler imagem", e);
        }

        repository.save(livro);
        log.info("LivroServiceImpl.inserirCapaDoLivro - concluído id={}", id);
    }

    @Override
    public byte[] buscarCapa(Long id) {
        log.debug("LivroServiceImpl.buscarCapa id={}", id);
        Livro livro = repository.findById(id).orElseThrow(
                () -> new LivroNaoEncontradoException("Livro não encontrado"));

        if (livro.getCapaDoLivro() == null) {
            throw new CapaNaoCadastradaException("Capa não cadastrada");
        } else {
            return livro.getCapaDoLivro();
        }
    }

    @Override
    public LivroResponse buscarIsbn(String isbn) {
        log.info("LivroServiceImpl.buscarIsbn isbn={}", isbn);
        Livro livro = repository.findByIsbn(isbn).orElseThrow(
                () -> new IsbnNaoEncontradoException("Não há nenhum livro cadastrado com o código ISBN informado"));
        return mapper.toResponse(livro);
    }

    @Override
    public Page<LivroCardResponse> buscar(String titulo, String editora, String autor, Pageable pageable) {
        log.info("LivroServiceImpl.buscar titulo={} editora={} autor={}", titulo, editora, autor);
        Specification<Livro> filtro = LivroFiltro.filtro(titulo, editora, autor);
        Page<LivroCardResponse> livros = repository.findAll(filtro, pageable).map(mapper::toCardResponse);
        if (livros.isEmpty()) {
            throw new LivroNaoEncontradoException("Nenhum livro encontrado para essa busca!");
        }
        return livros;
    }

    protected void validarCapaDoLivro(MultipartFile capaDoLivro) {
        try {
            log.debug("validarCapaDoLivro - iniciando size={} contentType={}",
                    capaDoLivro != null ? capaDoLivro.getSize() : 0,
                    capaDoLivro != null ? capaDoLivro.getContentType() : null);

            if (capaDoLivro == null || capaDoLivro.isEmpty()) {
                log.debug("validarCapaDoLivro - arquivo ausente ou vazio");
                return;
            }

            long tamanhoMaximo = 10 * 1024 * 1024;
            if (capaDoLivro.getSize() > tamanhoMaximo) {
                log.warn("validarCapaDoLivro - imagem muito grande size={}", capaDoLivro.getSize());
                throw new CapaForaDePadraoException("Imagem excede o tamanho máximo de 10MB");
            }

            List<String> tiposPermitidos = List.of(
                    "image/jpeg",
                    "image/jpg",
                    "image/png");

            if (capaDoLivro.getContentType() == null ||
                    !tiposPermitidos.contains(capaDoLivro.getContentType())) {
                log.warn("validarCapaDoLivro - tipo inválido contentType={}", capaDoLivro.getContentType());
                throw new CapaForaDePadraoException("Formato inválido. Use JPG ou PNG");
            }

            BufferedImage imagem = ImageIO.read(capaDoLivro.getInputStream());
            if (imagem == null) {
                log.warn("validarCapaDoLivro - arquivo não é uma imagem válida");
                throw new CapaForaDePadraoException("Arquivo enviado não é uma imagem válida");
            }

            log.debug("validarCapaDoLivro - validação concluída");

        } catch (IOException e) {
            log.error("Erro ao validar capa do livro", e);
            throw new CapaForaDePadraoException("Erro ao processar imagem");
        }
    }

    @Override
    public Livro buscar(Long id) {
        log.debug("LivroServiceImpl.buscar id={}", id);
        return repository.findById(id).orElseThrow(
                () -> new LivroNaoEncontradoException("Livro não cadastrado."));
    }

    @Override
    public Page<LivroDetalhadoResponse> getLivrosDoUsuario(Pageable pageable) {
        Long id = loginService.getUsuarioLogado().getUsuario().getId();
        log.debug("LivroServiceImpl.getLivrosDoUsuario - iniciando id={}", id);
        Page<LivroDetalhadoResponse> livros = usuarioLivroRepository.findLivrosByUsuarioId(id, pageable)
                .map(mapper::toLivroDetalhadoResponse);
        log.debug("LivroServiceImpl.getLivrosDoUsuario - concluído id={} count={}", id, livros.getTotalElements());
        return livros;
    }

    @Override
    public Page<LivroTelaLeituraResponse> getLivrosTelaDeLeituraDoUsuario(Pageable pageable) {
        Long id = loginService.getUsuarioLogado().getUsuario().getId();
        List<UsuarioLivro> usuarioLivros = usuarioLivroRepository.findAllByUsuarioId(id, pageable).stream().toList();
        List<LivroTelaLeituraResponse> resposta = new ArrayList<>();

        for (UsuarioLivro usuarioLivro : usuarioLivros) {
            Livro livro = usuarioLivro.getLivro();
            resposta.add(mapper.toLivroTelaLeituraResponse(livro, usuarioLivro.getStatus()));
        }
        log.debug("LivroServiceImpl.getLivrosTelaDeLeituraDoUsuario id={} count={}", id, resposta.size());
        Page<LivroTelaLeituraResponse> page = new PageImpl<>(resposta, pageable, resposta.size());
        return page;
    }

    @Override
    public void alterarStatusDoLivroNoUsuario(Long id, LivroStatus status) {
        Long idUsuario = loginService.getUsuarioLogado().getUsuario().getId();
        log.info("LivroServiceImpl.alterarStatusDoLivroNoUsuario - idLivro={} idUsuario={} status={}", id, idUsuario,
                status);
        Optional<UsuarioLivro> usuarioLivro = usuarioLivroRepository.findByLivro_IdAndUsuario_Id(id, idUsuario);
        if (usuarioLivro.isEmpty()) {
            log.warn(
                    "LivroServiceImpl.alterarStatusDoLivroNoUsuario - livro não encontrado na estante idLivro={} idUsuario={}",
                    id, idUsuario);
            throw new LivroNaoEncontradoException("O usuario não possue o livro na estante.");
        }
        usuarioLivro.get().setStatus(status);
        usuarioLivroRepository.save(usuarioLivro.get());
        log.info("LivroServiceImpl.alterarStatusDoLivroNoUsuario - status atualizado idLivro={} idUsuario={} status={}",
                id, idUsuario, status);
    }
}