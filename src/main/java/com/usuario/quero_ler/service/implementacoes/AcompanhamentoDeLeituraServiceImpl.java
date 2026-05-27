package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoRequestDto;
import com.usuario.quero_ler.dtos.leitura.AcompanhamentoResponseDto;
import com.usuario.quero_ler.exceptions.especies.DadosDiarioInvalidoException;
import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.repository.AcompanhamentoDeLeituraRepository;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AcompanhamentoDeLeituraServiceImpl implements AcompanhamentoDeLeituraService {

    private final DiarioDeLeituraRepository diarioRepository;
    private final AcompanhamentoDeLeituraRepository acompanhamentoRepository;

    @Override
    @Transactional
    public List<AcompanhamentoResponseDto> listarPorLivro(Long livroId) {
        List<AcompanhamentoDeLeitura> lista = acompanhamentoRepository.findByLivroIdWithJoins(livroId);
        List<AcompanhamentoResponseDto> resp = new ArrayList<>();
        for (AcompanhamentoDeLeitura a : lista) {
            Long usuarioId = null;
            if (a.getDiarioDeLeitura() != null && a.getDiarioDeLeitura().getUsuarioLivro() != null
                    && a.getDiarioDeLeitura().getUsuarioLivro().getUsuario() != null) {
                usuarioId = a.getDiarioDeLeitura().getUsuarioLivro().getUsuario().getId();
            }
            Long diarioId = a.getDiarioDeLeitura() != null ? a.getDiarioDeLeitura().getId() : null;
            resp.add(new AcompanhamentoResponseDto(a.getId(), a.getPaginaInicial(), a.getPaginaFinal(),
                    a.getComentario(), diarioId, usuarioId));
        }
        return resp;
    }

    @Override
    @Transactional
    public List<AcompanhamentoResponseDto> listarPorUsuario(Long usuarioId) {
        List<AcompanhamentoDeLeitura> lista = acompanhamentoRepository.findByUsuarioIdWithJoins(usuarioId);
        List<AcompanhamentoResponseDto> resp = new ArrayList<>();
        for (AcompanhamentoDeLeitura a : lista) {
            Long uId = null;
            if (a.getDiarioDeLeitura() != null && a.getDiarioDeLeitura().getUsuarioLivro() != null
                    && a.getDiarioDeLeitura().getUsuarioLivro().getUsuario() != null) {
                uId = a.getDiarioDeLeitura().getUsuarioLivro().getUsuario().getId();
            }
            Long diarioId = a.getDiarioDeLeitura() != null ? a.getDiarioDeLeitura().getId() : null;
            resp.add(new AcompanhamentoResponseDto(a.getId(), a.getPaginaInicial(), a.getPaginaFinal(),
                    a.getComentario(), diarioId, uId));
        }
        return resp;
    }

    @Transactional
    @Override
    public void adicionarComentario(Long diarioId, AcompanhamentoRequestDto dto) {
        if (dto == null) {
            throw new DadosDiarioInvalidoException("Payload do acompanhamento está vazio.");
        }

        DiarioDeLeitura diario = diarioRepository.findById(diarioId)
                .orElseThrow(() -> new DadosDiarioInvalidoException("Diário de leitura não encontrado."));

        AcompanhamentoDeLeitura acompanhamento = AcompanhamentoDeLeitura.builder()
                .paginaInicial(dto.paginaInicial())
                .paginaFinal(dto.paginaFinal())
                .comentario(dto.comentario())
                .diarioDeLeitura(diario)
                .build();

        acompanhamentoRepository.save(acompanhamento);
        diario.adicionarComentario(acompanhamento);
        diarioRepository.save(diario);
    }
}
