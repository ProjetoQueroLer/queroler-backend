package com.usuario.quero_ler.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfTest {

    @Test
    void cpfValidoSemFormatacao() {
        assertTrue(Cpf.isValid("49618203000"));
    }

    @Test
    void cpfValidoComFormatacao() {
        assertTrue(Cpf.isValid("496.182.030-00"));
    }

    @Test
    void cpfInvalidoNuloOuVazio() {
        assertFalse(Cpf.isValid(null));
        assertFalse(Cpf.isValid(""));
    }

    @Test
    void cpfInvalidoTodosDigitosIguais() {
        assertFalse(Cpf.isValid("11111111111"));
        assertFalse(Cpf.isValid("00000000000"));
    }

    @Test
    void cpfInvalidoChecksumIncorreto() {
        assertFalse(Cpf.isValid("49618203001"));
    }
}
