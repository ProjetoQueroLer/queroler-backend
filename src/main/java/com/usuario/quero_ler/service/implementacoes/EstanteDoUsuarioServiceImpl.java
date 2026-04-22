package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.livro.LivroTelaLeituraResponse;
import com.usuario.quero_ler.enums.LivroStatus;
import com.usuario.quero_ler.mappers.LivroMapper;
import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import com.usuario.quero_ler.service.EstanteDoUsuarioService;
import com.usuario.quero_ler.service.LivroService;
import com.usuario.quero_ler.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EstanteDoUsuarioServiceImpl implements EstanteDoUsuarioService {
    private final LivroService livroService;
    private final UsuarioService usuarioService;
    private final UsuarioLivroRepository repository;
    private final LivroMapper livroMapper;

    @Override
    public void adicionar(Long idUsuario, Long idLivro) {

    }

    @Override
    public Page<LivroTelaLeituraResponse> lista(Long id, Pageable pageable) {

        Page<UsuarioLivro> pageUsuarioLivros = repository.findAllByUsuarioId(id, pageable);

        List<LivroTelaLeituraResponse> resposta = new ArrayList<>();

        for (UsuarioLivro usuarioLivro : pageUsuarioLivros.getContent()) {
            resposta.add(
                    livroMapper.toLivroTelaLeituraResponse(
                            usuarioLivro.getLivro(),
                            usuarioLivro.getStatus()
                    )
            );
        }

        return new PageImpl<>(resposta, pageable, pageUsuarioLivros.getTotalElements());
    }

    @Override
    public void mudarStatus(Long idUsuario, String isbn, LivroStatus status) {
    }
}