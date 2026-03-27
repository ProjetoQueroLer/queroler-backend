package com.usuario.quero_ler.exceptions.especies;

public class IsbnNaoEncontradoException extends RuntimeException{
    public IsbnNaoEncontradoException(String mensagem) {
    super(mensagem);
    }
}