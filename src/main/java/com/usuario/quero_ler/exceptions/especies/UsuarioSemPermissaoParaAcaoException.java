package com.usuario.quero_ler.exceptions.especies;

public class UsuarioSemPermissaoParaAcaoException extends RuntimeException{
    public UsuarioSemPermissaoParaAcaoException(String mensagem) {
    super(mensagem);
    }
}