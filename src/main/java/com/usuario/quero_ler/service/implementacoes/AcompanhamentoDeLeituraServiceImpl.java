package com.usuario.quero_ler.service.implementacoes;

import com.usuario.quero_ler.dtos.leitura.AcompanhamentoRequestDto;
import com.usuario.quero_ler.exceptions.especies.DadosDiarioInvalidoException;
import com.usuario.quero_ler.models.AcompanhamentoDeLeitura;
import com.usuario.quero_ler.models.DiarioDeLeitura;
import com.usuario.quero_ler.repository.AcompanhamentoDeLeituraRepository;
import com.usuario.quero_ler.repository.DiarioDeLeituraRepository;
import com.usuario.quero_ler.service.AcompanhamentoDeLeituraService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AcompanhamentoDeLeituraServiceImpl implements AcompanhamentoDeLeituraService {

    private final DiarioDeLeituraRepository diarioRepository;
    private final AcompanhamentoDeLeituraRepository acompanhamentoRepository;

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
