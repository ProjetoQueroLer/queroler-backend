package com.usuario.quero_ler.controllers;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.service.LoginService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/logins")
public class LoginController {
    private final LoginService serviceI;

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDto autenticacaoDto,
            HttpServletResponse response) {
        log.info("POST /logins - login user={}", autenticacaoDto.user());
        serviceI.login(autenticacaoDto, response);
        log.info("Login realizado: user={}", autenticacaoDto.user());
        return ResponseEntity.ok().build();
    }
}
