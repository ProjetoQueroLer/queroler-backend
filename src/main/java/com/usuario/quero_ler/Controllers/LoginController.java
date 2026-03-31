package com.usuario.quero_ler.Controllers;

import com.usuario.quero_ler.dtos.login.LoginRequestDto;
import com.usuario.quero_ler.service.LoginServiceI;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/logins")
public class LoginController {
    private final LoginServiceI serviceI;

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDto autenticacaoDto, HttpServletResponse response) {
        serviceI.login(autenticacaoDto, response);
        return ResponseEntity.ok().build();
    }
}
