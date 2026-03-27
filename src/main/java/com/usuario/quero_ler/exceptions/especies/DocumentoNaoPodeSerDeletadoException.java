package com.usuario.quero_ler.exceptions.especies;

public class DocumentoNaoPodeSerDeletadoException extends RuntimeException{
    public DocumentoNaoPodeSerDeletadoException(String mensagem) {
    super(mensagem);
    }
}