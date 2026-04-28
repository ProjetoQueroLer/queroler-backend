package com.usuario.quero_ler.fixtures;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.enuns.UsuarioProfile;

public class LoginFixture {
    private static final String NOME = "admin";
    private static final String SENHA = "Teste123&";

    public static LoginRequestDto requestDto() {
        return new LoginRequestDto(NOME, SENHA);
    }
}
