package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.dtos.notificacao.NotificacaoRequestDto;
import com.usuario.quero_ler.enums.DocumentoTipo;
import com.usuario.quero_ler.exceptions.especies.DocumentoNaoEncontradoException;
import com.usuario.quero_ler.mappers.DocumentoMapper;
import com.usuario.quero_ler.models.Documento;
import com.usuario.quero_ler.repository.DocumentoRepository;
import com.usuario.quero_ler.service.DocumentoService;
import com.usuario.quero_ler.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentoServiceImpl implements DocumentoService {
    private final DocumentoRepository repository;
    private final DocumentoMapper mapper;
    private final NotificacaoService notificacaoService;

    @Override
    public DocumentoResponseDto criar(DocumentoRequestDto dto) {
        Documento documento = mapper.toEntity(dto);
        documento = repository.save(documento);
        gerarNonificacao(documento.getTipo().name());
        return mapper.toResponse(documento);
    }

    @Override
    public void alterar(Long id, DocumentoAlteracoesDto dto) {
        Documento documento = repository.findById(id).orElseThrow(
                () -> new DocumentoNaoEncontradoException("Documento não cadastrado.")
        );
        documento = mapper.toUpdate(documento, dto);
        gerarNonificacao(documento.getTipo().name());
        documento = repository.save(documento);
    }

    @Override
    public DocumentoResponseDto getTermosGeraisDeUso() {
        Documento documento = repository.findTopByTipoOrderByUltimaAlteracaoDesc(DocumentoTipo.TERMOS_GERAIS_DE_USO);
        return mapper.toResponse(documento);
    }

    protected void gerarNonificacao(String texto) {
        notificacaoService.criar(new NotificacaoRequestDto(texto));
    }

}
