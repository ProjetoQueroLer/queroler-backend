package com.usuario.quero_ler.service;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.dtos.usuario.UsuarioRequestDto;
import com.usuario.quero_ler.enuns.UsuarioProfile;
import com.usuario.quero_ler.models.User;

import jakarta.servlet.http.HttpServletResponse;

public interface LoginServiceI {
    User criar(UsuarioRequestDto dto, UsuarioProfile profile);
    void login(LoginRequestDto dto, HttpServletResponse response);
    User validarLogin();
    Boolean validarLogin(User user);
}
