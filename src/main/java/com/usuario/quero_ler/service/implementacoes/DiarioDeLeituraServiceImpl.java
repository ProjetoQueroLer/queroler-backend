package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;
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
import com.usuario.quero_ler.exceptions.especies.DiarioNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.UsuarioSemPermissaoParaAcaoException;

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

    @Transactional
    @Override
    public void atualizar(Long id, DiarioDeLeituraAtualizadoRequest dto) {
        validateUpdateDto(dto);

        DiarioDeLeitura diario = repository.findById(id)
                .orElseThrow(() -> new DiarioNaoEncontradoException("Diário de leitura não encontrado."));

        Long usuarioId = loginService.getUsuarioLogado().getUsuario().getId();
        verificarPropriedade(diario, usuarioId);

        aplicarAtualizacaoParcial(diario, dto);

        repository.save(diario);
    }

    private void verificarPropriedade(DiarioDeLeitura diario, Long usuarioId) {
        if (diario.getUsuarioLivro() == null || diario.getUsuarioLivro().getUsuario() == null ||
                !diario.getUsuarioLivro().getUsuario().getId().equals(usuarioId)) {
            throw new UsuarioSemPermissaoParaAcaoException("Usuário sem permissão para atualizar este diário.");
        }
    }

    private void aplicarAtualizacaoParcial(DiarioDeLeitura diario, DiarioDeLeituraAtualizadoRequest dto) {
        if (dto.inicioDaLeitura() != null) {
            diario.setInicioDaLeitura(dto.inicioDaLeitura());
        }

        if (dto.terminoDaLeitura() != null) {
            diario.setTerminoDaLeitura(dto.terminoDaLeitura());
        }

        if (dto.paginasLidas() != null) {
            diario.setPaginasLidas(dto.paginasLidas());
        }

        if (dto.nota() != null) {
            diario.setNota(dto.nota());
        }

        if (dto.tituloDaResenha() != null) {
            diario.setTituloDaResenha(dto.tituloDaResenha());
        }

        if (dto.resenha() != null) {
            diario.setResenha(dto.resenha());
        }
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
        if (dto.terminoDaLeitura() != null && dto.terminoDaLeitura().isAfter(now)) {
            throw new DadosDiarioInvalidoException("terminoDaLeitura não pode estar no futuro.");
        }

        if (dto.terminoDaLeitura() != null && dto.inicioDaLeitura().isAfter(dto.terminoDaLeitura())) {
            throw new DadosDiarioInvalidoException("terminoDaLeitura não pode ser anterior a inicioDaLeitura.");
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

    private void validateUpdateDto(DiarioDeLeituraAtualizadoRequest dto) {
        if (dto == null) {
            throw new DadosDiarioInvalidoException("Payload do diário está vazio.");
        }

        LocalDateTime now = LocalDateTime.now();

        if (dto.inicioDaLeitura() != null && dto.inicioDaLeitura().isAfter(now)) {
            throw new DadosDiarioInvalidoException("inicioDaLeitura não pode estar no futuro.");
        }

        if (dto.terminoDaLeitura() != null && dto.terminoDaLeitura().isAfter(now)) {
            throw new DadosDiarioInvalidoException("terminoDaLeitura não pode estar no futuro.");
        }

        if (dto.inicioDaLeitura() != null && dto.terminoDaLeitura() != null &&
                dto.inicioDaLeitura().isAfter(dto.terminoDaLeitura())) {
            throw new DadosDiarioInvalidoException("terminoDaLeitura não pode ser anterior a inicioDaLeitura.");
        }

        if (dto.paginasLidas() != null && dto.paginasLidas() < 0) {
            throw new DadosDiarioInvalidoException("paginasLidas não pode ser negativa.");
        }

        if (dto.nota() != null && (dto.nota() < 0 || dto.nota() > 5)) {
            throw new DadosDiarioInvalidoException("nota fora do intervalo permitido (0-5).");
        }
    }
}
