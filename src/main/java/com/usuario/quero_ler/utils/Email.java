package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.exceptions.especies.EmailInvalidoException;

public class Email {
    public static void validar(String email){
            if (email == null || email.isBlank()) {
                throw new EmailInvalidoException("Email é obrigatório.");
            }

        String regex = "^(?!.*\\.\\.)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,30}$";

            if (!email.matches(regex)) {
                throw new EmailInvalidoException("Email inválido.");
            }
        }
    }