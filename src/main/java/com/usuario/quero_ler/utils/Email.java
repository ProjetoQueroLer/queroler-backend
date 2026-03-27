package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.exceptions.especies.EmailInvalidoException;

public class Email {
    public static void validar(String email){
            if (email == null || email.isBlank()) {
                throw new EmailInvalidoException("Email é obrigatório.");
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new EmailInvalidoException("Email inválido.");
            }
        }
    }