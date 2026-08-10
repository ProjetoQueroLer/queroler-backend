package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraResponseDto;
import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraAtualizadoRequest;
import com.usuario.quero_ler.dtos.leitura.LivroAcompanhamentoResponseDto;
import com.usuario.quero_ler.mappers.DiarioLeituraMapper;
import com.usuario.quero_ler.exceptions.especies.DadosDiarioInvalidoException;
import java.time.LocalDateTime;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.models.Leitura;
import com.usuario.quero_ler.enums.LeituraStatus;
import com.usuario.quero_ler.exceptions.especies.DiarioJaExisteException;
import com.usuario.quero_ler.exceptions.especies.DiarioNaoEncontradoException;
import com.usuario.quero_ler.exceptions.especies.LeituraNaoEncontradaException;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.repository.LeituraRepository;
import com.usuario.quero_ler.service.DiarioDeLeituraService;
import com.usuario.quero_ler.service.LeituraService;
import com.usuario.quero_ler.service.LoginService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.usuario.quero_ler.exceptions.especies.UsuarioSemPermissaoParaAcaoException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiarioDeLeituraServiceImpl implements DiarioDeLeituraService {

    private final DiarioDeLeituraRepository repository;
    private final LeituraRepository leituraRepository;
    private final LoginService loginService;
    private final DiarioLeituraMapper diarioLeituraMapper;
    private final LeituraService leituraService;

    @Transactional
    @Override
    public void criar(DiarioDeLeituraRequestDto dto) {
        validateDto(dto);

        Long usuarioId = loginService.getUsuarioLogado().getUsuario().getId();

        Leitura leitura = leituraRepository
                .findByUsuarioIdAndLivroId(usuarioId, dto.livroId())
                .orElseThrow(() -> new LeituraNaoEncontradaException("Usuário/Livro não encontrado na estante."));

        leituraService.ControleStatusLeitura(leitura,
                dto.terminoDaLeitura() != null ? LeituraStatus.LIVROS_LIDOS : LeituraStatus.LIVROS_QUE_ESTOU_LENDO);
        leituraRepository.save(leitura);

        DiarioDeLeitura diario = DiarioDeLeitura.builder()
                .leitura(leitura)
                .inicioDaLeitura(dto.inicioDaLeitura())
                .terminoDaLeitura(dto.terminoDaLeitura())
                .paginasLidas(dto.paginasLidas())
                .nota(dto.nota() != null ? dto.nota() : 0.0)
                .tituloDaResenha(dto.tituloDaResenha())
                .resenha(dto.resenha())
                .spoiler(dto.spoiler() != null ? dto.spoiler() : false)
                .build();

        if (repository.existsByLeitura(leitura)) {
            throw new DiarioJaExisteException("Já existe um diário de leitura para este usuário e livro.");
        }

        repository.save(diario);
    }

    @Override
    public DiarioDeLeituraResponseDto buscarLeituraPorLivroEUsuario(Long livroId) {

        Long usuarioId = loginService.getUsuarioLogado().getUsuario().getId();

        DiarioDeLeitura diario = repository.findByUsuarioIdAndLivroId(usuarioId, livroId)
                .orElseThrow(() -> new DiarioNaoEncontradoException("Diario não encontrado!"));

        return diarioLeituraMapper.toResponse(diario);

    }

    @Override
    @Transactional
    public List<LivroAcompanhamentoResponseDto> listarEmAndamento() {
        Long usuarioId = loginService.getUsuarioLogado().getUsuario().getId();

        return repository.findEmAndamentoPorUsuario(usuarioId)
                .stream()
                .map(diarioLeituraMapper::toLivroAcompanhamentoResponse)
                .toList();
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

        if (dto.terminoDaLeitura() != null) {
            Leitura leitura = diario.getLeitura();
            leitura.setLido(true);
            leituraService.ControleStatusLeitura(leitura, LeituraStatus.LIVROS_LIDOS);
            leituraRepository.save(leitura);
        }
    }

    public void excluirDiarioDeLeitura(Long id) {
        DiarioDeLeitura diario = repository.findById(id)
                .orElseThrow(() -> new DiarioNaoEncontradoException("Diário de leitura não encontrado."));

        Long usuarioId = loginService.getUsuarioLogado().getUsuario().getId();
        verificarPropriedade(diario, usuarioId);

        Leitura leitura = diario.getLeitura();
        leitura.setLido(false);
        leituraService.ControleStatusLeitura(leitura, LeituraStatus.LIVROS_QUE_ESTOU_LENDO);
        leituraRepository.save(leitura);

        repository.delete(diario);
    }

    private void verificarPropriedade(DiarioDeLeitura diario, Long usuarioId) {
        if (diario.getLeitura() == null || diario.getLeitura().getUsuario() == null ||
                !diario.getLeitura().getUsuario().getId().equals(usuarioId)) {
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
