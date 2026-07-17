package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.models.MetaLeitura;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MetaLeituraMapper {
    public MetaLeitura toMetaLeitura(MetaRequestDto dto) {
        MetaLeitura novaMeta = new MetaLeitura();
        atualizarMetaLeitura(novaMeta, dto);
        return novaMeta;
    }

    public void atualizarMetaLeitura(MetaLeitura meta, MetaRequestDto dto) {
        Integer ano = LocalDate.now().getYear();

        meta.setAno(dto.ano() != null ? dto.ano() : ano);
        meta.setMetaLivrosAno(dto.metaLivrosAno() != null ? dto.metaLivrosAno() : 0);
        meta.setMetaLivrosMes(dto.metaLivrosMes() != null ? dto.metaLivrosMes() : 0);
        meta.setMetaPaginasDia(dto.metaPaginasDia() != null ? dto.metaPaginasDia() : 0);
    }
}
