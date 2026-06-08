package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.exceptions.especies.EmailInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailTest {

    @Test
    @DisplayName("Deve validar com sucesso um e-mail padrão válido")
    void deveValidarEmailValido() {
        String email = "usuario@provedor.com";
        assertDoesNotThrow(() -> Email.validar(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "teste.sobrenome@empresa.com.br",
            "usuario123@sub.dominio.org",
            "user+tag@gmail.com",
            "u@dominio.io"
    })
    @DisplayName("Deve aceitar diferentes formatos válidos de e-mail")
    void deveAceitarFormatosValidos(String emailValido) {
        assertDoesNotThrow(() -> Email.validar(emailValido));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o e-mail for null")
    void deveLancarExcecaoDeEmailNull() {
        String email = null;
        EmailInvalidoException exception = assertThrows(EmailInvalidoException.class,
                () -> Email.validar(email));
        assertEquals("Email é obrigatório.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "   ", "\n", "\t" })
    @DisplayName("Deve lançar exceção quando o e-mail for vazio ou composto apenas por espaços")
    void deveLancarExcecaoDeEmailVazio(String emailVazio) {
        EmailInvalidoException exception = assertThrows(EmailInvalidoException.class,
                () -> Email.validar(emailVazio));
        assertEquals("Email é obrigatório.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "qualquerCoisa.com",
            "@provedor.com",
            "usuario@",
            "usuario@provedor",
            "usuario@provedor.",
            "usuario @provedor.com",
            "usuario@@provedor.com"
    })
    @DisplayName("Deve lançar exceção para vários padrões de e-mails mal formatados")
    void deveLancarExcecaoDeEmailInvalido(String emailInvalido) {
        EmailInvalidoException exception = assertThrows(EmailInvalidoException.class,
                () -> Email.validar(emailInvalido));
        assertEquals("Email inválido.", exception.getMessage());
    }
}