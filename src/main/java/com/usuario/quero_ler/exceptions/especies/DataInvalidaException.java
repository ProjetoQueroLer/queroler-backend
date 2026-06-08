package com.usuario.quero_ler.exceptions.especies;

public class DataInvalidaException extends RuntimeException {

    public DataInvalidaException() {
        super("Data inválida");
    }

    public DataInvalidaException(String message) {
        super(message);
    }

    public DataInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
