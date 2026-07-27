package com.usuario.quero_ler.exceptions.especies;

public class LivroJaCadastradoException extends RuntimeException{
    public LivroJaCadastradoException(String mensagem) {
    super(mensagem);
    }
}