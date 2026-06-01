package com.usuario.quero_ler.exceptions.especies;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException() {
        super("CPF inválido.");
    }

    public CpfInvalidoException(String message) {
        super(message);
    }
}
