package com.usuario.quero_ler.exceptions.especies;

public class LeituraNaoEncontradaException extends RuntimeException {
    public LeituraNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
