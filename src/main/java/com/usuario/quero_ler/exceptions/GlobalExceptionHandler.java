package com.usuario.quero_ler.exceptions;

import com.usuario.quero_ler.exceptions.especies.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNaoCadastradoException.class)
    public ResponseEntity<Object> handlerEmailNaoCadastradoException(EmailNaoCadastradoException ex) {
        log.error("EmailNaoCadastradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CapaNaoCadastradaException.class)
    public ResponseEntity<Object> handlerCapaNaoCadastradaException(CapaNaoCadastradaException ex) {
        log.error("CapaNaoCadastradaException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(FotoNaoCadastradaException.class)
    public ResponseEntity<Object> handlerFotoNaoCadastradaException(FotoNaoCadastradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IsbnNaoEncontradoException.class)
    public ResponseEntity<Object> handlerIsbnNaoEncontradoException(IsbnNaoEncontradoException ex) {
        log.error("IsbnNaoEncontradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<Object> handlerUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        log.error("UsuarioNaoEncontradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NotificacaoNaoEncontradaException.class)
    public ResponseEntity<Object> handlerNotificacaoNaoEncontradaException(NotificacaoNaoEncontradaException ex) {
        log.error("NotificacaoNaoEncontradaException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DocumentoNaoEncontradoException.class)
    public ResponseEntity<Object> handlerDocumentoNaoEncontradoException(DocumentoNaoEncontradoException ex) {
        log.error("DocumentoNaoEncontradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ArquivoNaoEncontradoException.class)
    public ResponseEntity<Object> handlerArquivoNaoEncontrado(ArquivoNaoEncontradoException ex) {
        log.error("ArquivoNaoEncontradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ImagemNaoCarregaException.class)
    public ResponseEntity<Object> handlerImagemNaoCarregaException(ImagemNaoCarregaException ex) {
        log.error("ImagemNaoCarregaException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(LivroNaoEncontradoException.class)
    public ResponseEntity<Object> handlerLivroNaoEncontradoException(LivroNaoEncontradoException ex) {
        log.error("LivroNaoEncontradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handlerSenhaInvalidaException(SenhaInvalidaException ex) {
        log.error("SenhaInvalidaException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoAutenticadoException.class)
    public ResponseEntity<Object> handlerUsuarioNaoAutenticadoException(UsuarioNaoAutenticadoException ex) {
        log.error("UsuarioNaoAutenticadoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailInvalidoException.class)
    public ResponseEntity<Object> handlerEmailInvalidoException(EmailInvalidoException ex) {
        log.error("EmailInvalidoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Object> handlerEmailJaCadastradoException(EmailJaCadastradoException ex) {
        log.error("EmailJaCadastradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(GerarTokenException.class)
    public ResponseEntity<Object> handlerGerarTokenException(GerarTokenException ex) {
        log.error("GerarTokenException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(LerImagemException.class)
    public ResponseEntity<Object> handlerLerImagemException(LerImagemException ex) {
        log.error("LerImagemException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioSemPermissaoParaAcaoException.class)
    public ResponseEntity<Object> handlerUsuarioSemPermissaoParaAcaoException(UsuarioSemPermissaoParaAcaoException ex) {
        log.error("UsuarioSemPermissaoParaAcaoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioJaPossueOLivroException.class)
    public ResponseEntity<Object> handlerUsuarioJaPossueOLivroException(UsuarioJaPossueOLivroException ex) {
        log.error("UsuarioJaPossueOLivroException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioComPerfilInvalidoException.class)
    public ResponseEntity<Object> handlerUsuarioComPerfilInvalidoException(UsuarioComPerfilInvalidoException ex) {
        log.error("UsuarioComPerfilInvalidoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IsbnJaCadastradoException.class)
    public ResponseEntity<Object> handlerIsbnJaCadastradoException(IsbnJaCadastradoException ex) {
        log.error("IsbnJaCadastradoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CapaForaDePadraoException.class)
    public ResponseEntity<Object> handlerCapaForaDePadraoException(CapaForaDePadraoException ex) {
        log.error("CapaForaDePadraoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DocumentoNaoPodeSerDeletadoException.class)
    public ResponseEntity<Object> handlerDocumentoNaoPodeSerDeletadoException(DocumentoNaoPodeSerDeletadoException ex) {
        log.error("DocumentoNaoPodeSerDeletadoException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<Object> handlerCredenciaisInvalidasException(CredenciaisInvalidasException ex) {
        log.error("CredenciaisInvalidasException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(EnumInvalidoException.class)
    public ResponseEntity<String> handleEnumInvalido(EnumInvalidoException ex) {
        log.error("EnumInvalidoException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleEnumError(HttpMessageNotReadableException ex) {

        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException e
                && e.getTargetType().isEnum()) {

            Object[] valores = e.getTargetType().getEnumConstants();

            String mensagem = "Valor inválido. Valores permitidos: " + Arrays.toString(valores);
            log.error("HttpMessageNotReadableException - enum invalid: {}", mensagem, ex);
            return ResponseEntity.badRequest().body(mensagem);
        }

        log.error("HttpMessageNotReadableException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body("Erro ao interpretar JSON.");
    }
}