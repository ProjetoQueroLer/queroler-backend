package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.meta.MetaRequestDto;
import com.usuario.quero_ler.fixtures.MetaLeituraFixture;
import com.usuario.quero_ler.models.MetaLeitura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MetaLeituraMapperTest {

    @InjectMocks
    private MetaLeituraMapper mapper;

    @Test
    @DisplayName("Deve Converter uma meta leitura request em entidade.")
    void toEntity() {
        Integer proximoAno = LocalDate.now().plusYears(1).getYear();
        MetaRequestDto dto = MetaLeituraFixture.requestDto(proximoAno);

        MetaLeitura meta = mapper.toMetaLeitura(dto);

        assertEquals(dto.ano(),meta.getAno());
        assertEquals(dto.metaLivrosAno(), meta.getMetaLivrosAno());
        assertEquals(dto.metaLivrosMes(),meta.getMetaLivrosMes());
        assertEquals(dto.metaPaginasDia(), meta.getMetaPaginasDia());
    }
    @Test
    @DisplayName("Deve Converter uma meta leitura request em entidade, com ano corrente.")
    void toEntityAnoCorrente() {
        Integer anoCorrente = LocalDate.now().getYear();
        MetaRequestDto dto = MetaLeituraFixture.requestDto(null);

        MetaLeitura meta = mapper.toMetaLeitura(dto);

        assertEquals(anoCorrente,meta.getAno());
        assertEquals(dto.metaLivrosAno(), meta.getMetaLivrosAno());
        assertEquals(dto.metaLivrosMes(),meta.getMetaLivrosMes());
        assertEquals(dto.metaPaginasDia(), meta.getMetaPaginasDia());
    }
}
