package com.usuario.quero_ler.utils;

import com.usuario.quero_ler.exceptions.especies.SenhaInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SenhasTest {

    @Test
    @DisplayName("Deve gerar uma senha hash")
    void deveGerarUmaSenhaHash() {
        String senha = "T123#";

        String resposta = Senhas.gerar(senha);

        assertTrue(BCrypt.checkpw(senha, resposta));
    }

    @Test
    @DisplayName("Deve lançar excessão por estar vazia.")
    void deveLancarExcessaoPorEstarVazia() {
        String senha = null;

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                ()-> Senhas.validar(senha));

        assertEquals("Senha é obrigatória.", exception.getMessage());

    }

    @Test
    @DisplayName("Deve lançar excessão por não ter 8 caracteres.")
    void deveLancarExcessaoPorNaoRespeitarONumeroMinimoDeCaracteres() {
        String senha ="Test12&";

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                ()-> Senhas.validar(senha));

        assertEquals("A senha deve ter no mínimo 8 caracteres.", exception.getMessage());

    }

@Test
    @DisplayName("Deve lançar excessão por falta de letra maiuscula na senha.")
    void deveLancarExcessaoPorFaltaDeLetraMaiusculaNaSenhar() {
        String senha ="teste123&";

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                ()-> Senhas.validar(senha));

        assertEquals("A senha deve conter pelo menos uma letra maiúscula.", exception.getMessage());

    }

    @Test
    @DisplayName("Deve lançar excessão por falta de letra minusculas na senha.")
    void deveLancarExcessaoPorFaltaDeLetraMinusculasNaSenhar() {
        String senha = "TESTE123&";

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> Senhas.validar(senha));

        assertEquals("A senha deve conter pelo menos uma letra minúscula.", exception.getMessage());
    }


    @Test
    @DisplayName("Deve lançar excessão por falta de um número na senha.")
    void deveLancarExcessaoPorFaltaDeNumeroNaSenhar() {
        String senha = "TesteTeste&";

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> Senhas.validar(senha));

        assertEquals("A senha deve conter pelo menos um número.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar excessão por falta de um caractere especial na senha.")
    void deveLancarExcessaoPorFaltaDeCaractereEspecialNaSenhar() {
        String senha = "Teste1234";

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> Senhas.validar(senha));

        assertEquals("A senha deve conter pelo menos um caractere especial.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar excessão por senhas diferentes.")
    void deveLancarExcessaoPorSenhasDiferntes() {
        String senha = "Teste1234";

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> Senhas.validarIguais(senha, "99999999"));

        assertEquals("As senhas devem ser iguais.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve validar senhas .")
    void deveValidarSenhasIguais() {
        String senha = "Teste1234&";
        assertDoesNotThrow(() -> Senhas.validarIguais(senha, senha));
    }
}