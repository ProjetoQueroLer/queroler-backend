package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.dtos.meta.MetaResponseDto;
import com.usuario.quero_ler.models.MetaLeitura;

import java.time.LocalDate;

public class MetaLeituraFixture {
    private final static Long ID = 2L;
    private final static Integer META_LIVROS_ANO = 12;
    private final static Integer META_LIVROS_MES = 1;
    private final static Integer META_PAGINAS_DIA = 30;
    private final static Integer anoCorrente = LocalDate.now().getYear();

    public static MetaRequestDto requestDto(Integer ano) {
        return new MetaRequestDto(
                ano,
                META_LIVROS_ANO,
                META_LIVROS_MES,
                META_PAGINAS_DIA
        );
    }

    public static MetaLeitura metaLeitura() {
            return metaLeitura(requestDto(anoCorrente));
    }

    public static MetaLeitura metaLeitura(MetaRequestDto dto) {
        return MetaLeitura.builder()
                .id(ID)
                .ano(dto.ano() != null ? dto.ano() : anoCorrente)
                .metaLivrosAno(dto.metaLivrosAno())
                .metaLivrosMes(dto.metaLivrosMes())
                .metaPaginasDia(dto.metaPaginasDia())
                .usuario(UserFixture.entidadeCompleta())
                .build();

    }

    public static MetaResponseDto metaResponseDto() {
        return new MetaResponseDto(
                anoCorrente,
                META_LIVROS_ANO,
                META_LIVROS_MES,
                META_PAGINAS_DIA,
                null
        );
    }
}