package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.exceptions.especies.SenhaInvalidaException;
import org.mindrot.jbcrypt.BCrypt;

public class Senhas {
    public static String gerar(String senha) {
        validar(senha);
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static Boolean validar(String senha, String hash) {
        validar(senha);
        return BCrypt.checkpw(senha, hash);
    }

    public static void validar(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new SenhaInvalidaException("Senha é obrigatória.");
        }

        if (senha.length() < 8) {
            throw new SenhaInvalidaException("A senha deve ter no mínimo 8 caracteres.");
        }

        if (!senha.matches(".*[A-Z].*")) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos uma letra maiúscula.");
        }

        if (!senha.matches(".*[a-z].*")) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos uma letra minúscula.");
        }

        if (!senha.matches(".*\\d.*")) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos um número.");
        }

        if (!senha.matches(".*[@$!%*?&.#_-].*")) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos um caractere especial.");
        }
    }

    public static void validarIguais(String senha, String confirma) {
        if (!senha.equals(confirma)) {
            throw new SenhaInvalidaException("As senhas devem ser iguais.");
        }
        validar(senha);
    }
}