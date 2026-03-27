package com.usuario.quero_ler.exceptions.especies;

public class UsuarioNaoAutenticadoException extends RuntimeException{
    public UsuarioNaoAutenticadoException(String mensagem) {
    super(mensagem);
    }
}