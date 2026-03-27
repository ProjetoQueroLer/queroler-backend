package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.exceptions.especies.EmailInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class EmailTest {

    @Test
    @DisplayName("Deve lançar excessão email vazio")
    void deveLancarExcessaoDeEmailVazio() {
        String email = null;
        EmailInvalidoException exception = assertThrows(EmailInvalidoException.class,
                ()-> Email.validar(email));
        assertEquals("Email é obrigatório.",exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar excessão por email inválido")
    void deveLancarExcessaoDeEmailInvalido() {
        String email = "qualguerCoisa.com";
        EmailInvalidoException exception = assertThrows(EmailInvalidoException.class,
                ()-> Email.validar(email));
        assertEquals("Email inválido.",exception.getMessage());
    }
}