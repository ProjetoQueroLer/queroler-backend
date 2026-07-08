package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.models.MetaLeitura;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class MetaLeituraMapper {
    public MetaLeitura toMetaLeitura(MetaRequestDto dto){
        Integer ano = LocalDate.now().getYear();
        MetaLeitura novaMeta= new MetaLeitura();
        novaMeta.setAno(dto.ano() != null ? dto.ano() : ano);
        novaMeta.setMetaLivrosAno(dto.metaLivrosAno() != null ? dto.metaLivrosAno() : 0);
        novaMeta.setMetaLivrosMes(dto.metaLivrosMes() != null ? dto.metaLivrosMes() : 0);
        novaMeta.setMetaPaginasDia(dto.metaPaginasDia() != null ? dto.metaPaginasDia() : 0);
        return novaMeta;
    }
}
