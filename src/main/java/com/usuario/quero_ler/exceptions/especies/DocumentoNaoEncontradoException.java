package com.usuario.quero_ler.exceptions.especies;

public class DocumentoNaoEncontradoException extends RuntimeException{
    public DocumentoNaoEncontradoException(String mensagem) {
    super(mensagem);
    }
}