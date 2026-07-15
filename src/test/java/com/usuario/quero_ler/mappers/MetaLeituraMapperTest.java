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

        assertMeta(dto, meta, proximoAno);
    }

    @Test
    @DisplayName("Deve Converter uma meta leitura request em entidade, com ano corrente.")
    void toEntityAnoCorrente() {
        Integer anoCorrente = LocalDate.now().getYear();
        MetaRequestDto dto = MetaLeituraFixture.requestDto(null);

        MetaLeitura meta = mapper.toMetaLeitura(dto);

        assertMeta(dto, meta, anoCorrente);
    }

    @Test
    @DisplayName("Deve atualizar uma entidade MetaLeitura a partir do DTO.")
    void deveAtualizarMetaLeitura() {
        Integer proximoAno = LocalDate.now().plusYears(1).getYear();

        MetaRequestDto dto = MetaLeituraFixture.requestDto(proximoAno);
        MetaLeitura meta = new MetaLeitura();

        mapper.atualizarMetaLeitura(meta, dto);

        assertMeta(dto, meta, proximoAno);
    }

    @Test
    @DisplayName("Deve atualizar uma entidade MetaLeitura com o ano corrente.")
    void deveAtualizarMetaLeituraComAnoCorrente() {
        Integer anoCorrente = LocalDate.now().getYear();

        MetaRequestDto dto = MetaLeituraFixture.requestDto(null);
        MetaLeitura meta = new MetaLeitura();

        mapper.atualizarMetaLeitura(meta, dto);

        assertMeta(dto, meta, anoCorrente);
    }

    private void assertMeta(MetaRequestDto dto, MetaLeitura meta, Integer anoEsperado) {
        assertEquals(anoEsperado, meta.getAno());
        assertEquals(dto.metaLivrosAno(), meta.getMetaLivrosAno());
        assertEquals(dto.metaLivrosMes(), meta.getMetaLivrosMes());
        assertEquals(dto.metaPaginasDia(), meta.getMetaPaginasDia());
    }
}
