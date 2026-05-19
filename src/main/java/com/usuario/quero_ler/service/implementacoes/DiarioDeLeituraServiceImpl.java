package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.exceptions.especies.UsuarioLivroNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.DadosDiarioInvalidoException;
import java.time.LocalDateTime;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.UsuarioLivro;
import com.usuario.quero_ler.exceptions.especies.DiarioJaExisteException;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.repository.UsuarioLivroRepository;
import com.usuario.quero_ler.service.DiarioDeLeituraService;
import com.usuario.quero_ler.service.LoginService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DiarioDeLeituraServiceImpl implements DiarioDeLeituraService {

    private final DiarioDeLeituraRepository repository;
    private final UsuarioLivroRepository usuarioLivroRepository;
    private final LoginService loginService;

    @Transactional
    @Override
    public void criar(DiarioDeLeituraRequestDto dto) {
        validateDto(dto);

        Long usuarioId = loginService.getUsuarioLogado().getUsuario().getId();

        UsuarioLivro usuarioLivro = usuarioLivroRepository
                .findByUsuarioIdAndLivroId(usuarioId, dto.livroId())
                .orElseThrow(() -> new UsuarioLivroNaoEncontradoException("Usuário/Livro não encontrado na estante."));

        DiarioDeLeitura diario = DiarioDeLeitura.builder()
                .usuarioLivro(usuarioLivro)
                .inicioDaLeitura(dto.inicioDaLeitura())
                .terminoDaLeitura(dto.terminoDaLeitura())
                .paginasLidas(dto.paginasLidas())
                .nota(dto.nota())
                .tituloDaResenha(dto.tituloDaResenha())
                .resenha(dto.resenha())
                .build();

        if (repository.existsByUsuarioLivro(usuarioLivro)) {
            throw new DiarioJaExisteException("Já existe um diário de leitura para este usuário e livro.");
        }

        repository.save(diario);
    }

    private void validateDto(DiarioDeLeituraRequestDto dto) {
        if (dto == null) {
            throw new DadosDiarioInvalidoException("Payload do diário está vazio.");
        }

        validateRequiredFields(dto);
        validateDates(dto);
        validateNumericFields(dto);
    }

    private void validateRequiredFields(DiarioDeLeituraRequestDto dto) {
        if (dto.livroId() == null) {
            throw new DadosDiarioInvalidoException("livroId é obrigatório.");
        }

        if (dto.inicioDaLeitura() == null) {
            throw new DadosDiarioInvalidoException("inicioDaLeitura é obrigatório.");
        }
    }

    private void validateDates(DiarioDeLeituraRequestDto dto) {
        LocalDateTime now = LocalDateTime.now();
        if (dto.inicioDaLeitura().isAfter(now)) {
            throw new DadosDiarioInvalidoException("inicioDaLeitura não pode estar no futuro.");
        }

        if (dto.terminoDaLeitura() != null) {
            if (dto.terminoDaLeitura().isAfter(now)) {
                throw new DadosDiarioInvalidoException("terminoDaLeitura não pode estar no futuro.");
            }

            if (dto.inicioDaLeitura().isAfter(dto.terminoDaLeitura())) {
                throw new DadosDiarioInvalidoException("terminoDaLeitura não pode ser anterior a inicioDaLeitura.");
            }
        }
    }

    private void validateNumericFields(DiarioDeLeituraRequestDto dto) {
        if (dto.paginasLidas() != null && dto.paginasLidas() < 0) {
            throw new DadosDiarioInvalidoException("paginasLidas não pode ser negativa.");
        }

        if (dto.nota() != null && (dto.nota() < 0 || dto.nota() > 5)) {
            throw new DadosDiarioInvalidoException("nota fora do intervalo permitido (0-5).");
        }
    }
}
