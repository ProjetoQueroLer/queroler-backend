package com.usuario.quero_ler.exceptions.especies;

public class EmailNaoCadastradoException extends RuntimeException{
    public EmailNaoCadastradoException(String mensagem) {
    super(mensagem);
    }
}