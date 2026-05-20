package com.usuario.quero_ler.mappers;

import org.springframework.stereotype.Component;

import com.usuario.quero_ler.dtos.leitura.DiarioDeLeituraRequestDto;
import com.usuario.quero_ler.models.DiarioDeLeitura;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiarioLeituraMapper {

    public DiarioDeLeitura toEntity(DiarioDeLeituraRequestDto dto) {
        if (dto == null) {
            return null;
        }

        DiarioDeLeitura diario = new DiarioDeLeitura();
        
        // 1. Campos diretos e simples
        diario.setInicioDaLeitura(dto.inicioDaLeitura());
        diario.setPaginasLidas(dto.paginasLidas());
        diario.setNota(dto.nota());
        diario.setTituloDaResenha(dto.tituloDaResenha());
        diario.setResenha(dto.resenha());

        return diario;
    }
}
