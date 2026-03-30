package com.usuario.quero_ler.exceptions.especies;

public class LivroNaoEncontradoException extends RuntimeException{
    public LivroNaoEncontradoException(String mensagem) {
    super(mensagem);
    }
}