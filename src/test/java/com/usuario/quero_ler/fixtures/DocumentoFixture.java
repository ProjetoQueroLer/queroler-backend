package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.documento.DocumentoRequestDto;
import com.usuario.quero_ler.dtos.documento.DocumentoResponseDto;
import com.usuario.quero_ler.enuns.DocumentoTipo;
import com.usuario.quero_ler.models.Documento;

import java.time.LocalDateTime;

public class DocumentoFixture {
    private static final Long ID = 2L;
    private static final String TITULO = "TERMOS_GERAIS_DE_USO";
    private static final DocumentoTipo TIPO = DocumentoTipo.TERMOS_GERAIS_DE_USO;
    private static final String CONTEUDO = "Conteúdo do documento";
    private static final LocalDateTime ULTIMA_ALTERACAO = LocalDateTime.now();

    public static DocumentoRequestDto requestDto(){
        return new DocumentoRequestDto(TITULO,TIPO,CONTEUDO);
    }

    public static Documento entity(){
        return new Documento(ID,TITULO,TIPO,CONTEUDO,ULTIMA_ALTERACAO);
    }

    public static DocumentoResponseDto responseDto(){
        return new DocumentoResponseDto(ID,TITULO,TIPO,CONTEUDO,ULTIMA_ALTERACAO);
    }
    public static DocumentoResponseDto responseDto(Documento doc){
        return new DocumentoResponseDto(doc.getId(),
                doc.getTitulo(), doc.getTipo(), doc.getConteudo(), doc.getUltimaAlteracao());
    }
}