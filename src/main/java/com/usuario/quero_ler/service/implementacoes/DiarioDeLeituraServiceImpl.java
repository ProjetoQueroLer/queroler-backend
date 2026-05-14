package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.exceptions.especies.UsuarioLivroNaoEncontradoException;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import com.usuario.quero_ler.service.DiarioDeLeituraService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DiarioDeLeituraServiceImpl implements DiarioDeLeituraService {

    private final DiarioDeLeituraRepository repository;
    private final UsuarioLivroRepository usuarioLivroRepository;

    @Transactional
    @Override
    public void criar(DiarioDeLeituraRequestDto dto) {
        UsuarioLivro usuarioLivro = usuarioLivroRepository
                .findByUsuarioIdAndLivroId(dto.usuarioId(), dto.livroId())
                .orElseThrow(() -> new UsuarioLivroNaoEncontradoException("Usuário/Livro não encontrado na estante."));

        DiarioDeLeitura diario = DiarioDeLeitura.builder()
                .usuarioLivro(usuarioLivro)
                .inicioDaLeitura(dto.inicioDaLeitura())
                .terminoDaLeitura(dto.terminoDaLeitura())
                .paginasLidas(dto.paginasLidas())
                .nota(dto.nota() == null ? 0 : dto.nota())
                .tituloDaResenha(dto.tituloDaResenha())
                .resenha(dto.resenha())
                .build();

        repository.save(diario);
    }
}
