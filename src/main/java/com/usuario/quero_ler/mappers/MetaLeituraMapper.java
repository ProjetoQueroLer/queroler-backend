package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.livro.LivroResponse;
import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.dtos.meta.MetaResponseDto;
import com.usuario.quero_ler.models.LivroMeta;
import com.usuario.quero_ler.models.MetaLeitura;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MetaLeituraMapper {

    private final LivroMapper livroMapper;

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

    public MetaResponseDto metaResponseDto(MetaLeitura metaLeitura){
        return new MetaResponseDto(
                metaLeitura.getAno(),
                metaLeitura.getMetaLivrosAno(),
                metaLeitura.getMetaLivrosMes(),
                metaLeitura.getMetaPaginasDia(),
                getLivros(metaLeitura)
        );
    }

    protected List<LivroResponse> getLivros(MetaLeitura metaLeitura) {
        return metaLeitura.getLivrosMeta().stream()
                .map(LivroMeta::getLivro)
                .map(livroMapper::toResponse)
                .toList();
    }
}
