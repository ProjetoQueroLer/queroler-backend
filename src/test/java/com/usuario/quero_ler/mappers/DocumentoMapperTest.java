package com.usuario.quero_ler.mappers;

import com.usuario.quero_ler.dtos.documento.DocumentoAlteracoesDto;
import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.fixtures.DocumentoFixture;
import com.usuario.quero_ler.models.Documento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentoMapperTest {

    @InjectMocks
    private DocumentoMapper mapper;

    @Test
    @DisplayName("Deve Converter o documento em entidade.")
    void toEntity() {
        DocumentoRequestDto dto = DocumentoFixture.requestDto();
        LocalDateTime base = LocalDateTime.now();

        Documento resposta = mapper.toEntity(dto);

        assertNull(resposta.getId());
        assertEquals(dto.titulo(), resposta.getTitulo());
        assertEquals(dto.tipo(), resposta.getTipo());
        assertEquals(dto.conteudo(), resposta.getConteudo());

        assertNotNull(resposta.getUltimaAlteracao());
        assertTrue(Duration.between(base, resposta.getUltimaAlteracao()).abs().toSeconds() <= 2);
    }

    @Test
    @DisplayName("Deve converter um documento entidade em documento de resposta")
    void toResponse() {
        Documento documento = DocumentoFixture.entity();

        DocumentoResponseDto resposta = mapper.toResponse(documento);

        assertEquals(documento.getId(), resposta.id());
        assertEquals(documento.getTitulo(), resposta.titulo());
        assertEquals(documento.getTipo(), resposta.tipo());
        assertEquals(documento.getConteudo(), resposta.conteudo());

        assertEquals(documento.getUltimaAlteracao(), resposta.ultimaAlteracao());
    }

    @Test
    @DisplayName("Deve fazer a atualização do documento")
    void toUpdate() {
        Documento documento = DocumentoFixture.entity();
        DocumentoAlteracoesDto dto = new DocumentoAlteracoesDto("Titulo Alterado", null, "conteudo alterado");
        LocalDateTime base = LocalDateTime.now();

        Documento resposta = mapper.toUpdate(documento, dto);

        assertEquals(documento.getId(), resposta.getId());
        assertEquals(dto.titulo(), resposta.getTitulo());
        assertEquals(documento.getTipo(), resposta.getTipo());
        assertEquals(dto.conteudo(), resposta.getConteudo());

        assertNotNull(resposta.getUltimaAlteracao());
        assertTrue(Duration.between(base, resposta.getUltimaAlteracao()).abs().toSeconds() <= 2);
    }
}