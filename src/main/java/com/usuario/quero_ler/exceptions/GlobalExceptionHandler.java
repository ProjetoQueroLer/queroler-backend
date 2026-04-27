package com.usuario.quero_ler.exceptions;

import com.usuario.quero_ler.exceptions.especies.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNaoCadastradoException.class)
    public ResponseEntity<Object> handlerEmailNaoCadastradoException(EmailNaoCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CapaNaoCadastradaException.class)
    public ResponseEntity<Object> handlerCapaNaoCadastradaException(CapaNaoCadastradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IsbnNaoEncontradoException.class)
    public ResponseEntity<Object> handlerIsbnNaoEncontradoException(IsbnNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Object> handlerUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NotificacaoNaoEncontradaException.class)
    public ResponseEntity<Object> handlerNotificacaoNaoEncontradaException(NotificacaoNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DocumentoNaoEncontradoException.class)
    public ResponseEntity<Object> handlerDocumentoNaoEncontradoException(DocumentoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ArquivoNaoEncontradoException.class)
    public ResponseEntity<Object> handlerArquivoNaoEncontrado(ArquivoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ImagemNaoCarregaException.class)
    public ResponseEntity<Object> handlerImagemNaoCarregaException(ImagemNaoCarregaException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(LivroNaoEncontradoException.class)
    public ResponseEntity<Object> handlerLivroNaoEncontradoException(LivroNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handlerSenhaInvalidaException(SenhaInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoAutenticadoException.class)
    public ResponseEntity<Object> handlerUsuarioNaoAutenticadoException(UsuarioNaoAutenticadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<Object> handlerEmailInvalidoException(EmailInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Object> handlerEmailJaCadastradoException(EmailJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(GerarTokenException.class)
    public ResponseEntity<Object> handlerGerarTokenException(GerarTokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(LerImagemException.class)
    public ResponseEntity<Object> handlerLerImagemException(LerImagemException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioSemPermissaoParaAcaoException.class)
    public ResponseEntity<Object> handlerUsuarioSemPermissaoParaAcaoException(UsuarioSemPermissaoParaAcaoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioJaPossueOLivroException.class)
    public ResponseEntity<Object> handlerUsuarioJaPossueOLivroException(UsuarioJaPossueOLivroException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioComPerfilInvalidoException.class)
    public ResponseEntity<Object> handlerUsuarioComPerfilInvalidoException(UsuarioComPerfilInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IsbnJaCadastradoException.class)
    public ResponseEntity<Object> handlerIsbnJaCadastradoException(IsbnJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CapaForaDePadraoException.class)
    public ResponseEntity<Object> handlerCapaForaDePadraoException(CapaForaDePadraoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DocumentoNaoPodeSerDeletadoException.class)
    public ResponseEntity<Object> handlerDocumentoNaoPodeSerDeletadoException(DocumentoNaoPodeSerDeletadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Object> handlerCredenciaisInvalidasException(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(EnumInvalidoException.class)
    public ResponseEntity<String> handleEnumInvalido(EnumInvalidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleEnumError(HttpMessageNotReadableException ex) {

        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException e
                && e.getTargetType().isEnum()) {

            Object[] valores = e.getTargetType().getEnumConstants();

            String mensagem = "Valor inválido. Valores permitidos: " + Arrays.toString(valores);

            return ResponseEntity.badRequest().body(mensagem);
        }

        return ResponseEntity.badRequest().body("Erro ao interpretar JSON.");
    }
}