package com.usuario.quero_ler.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.usuario.quero_ler.exceptions.especies.*;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String tamanhoMaximo;

    @ExceptionHandler(EmailNaoCadastradoException.class)
    public ResponseEntity<Object> handlerEmailNaoCadastradoException(EmailNaoCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CapaNaoCadastradaException.class)
    public ResponseEntity<Object> handlerCapaNaoCadastradaException(CapaNaoCadastradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(FotoNaoCadastradaException.class)
    public ResponseEntity<Object> handlerFotoNaoCadastradaException(FotoNaoCadastradaException ex) {
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

    @ExceptionHandler(UsuarioLivroNaoEncontradoException.class)
    public ResponseEntity<Object> handlerUsuarioLivroNaoEncontrado(UsuarioLivroNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DiarioNaoEncontradoException.class)
    public ResponseEntity<Object> handlerDiarioNaoEncontrado(DiarioNaoEncontradoException ex) {
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

    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<Object> handlerCpfInvalidoException(CpfInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(GerarTokenException.class)
    public ResponseEntity<Object> handlerGerarTokenException(GerarTokenException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(LerImagemException.class)
    public ResponseEntity<Object> handlerLerImagemException(LerImagemException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<Object> handlerCpfJaCadastradoException(CpfJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Object> handlerEmailJaCadastradoException(EmailJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
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
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ex.getMessage());
    }

    @ExceptionHandler(AusenciaDeDadosException.class)
    public ResponseEntity<Object> handlerAusenciaDeDadosException(AusenciaDeDadosException ex) {
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

    @ExceptionHandler(DadosDiarioInvalidoException.class)
    public ResponseEntity<Object> handlerDadosDiarioInvalido(
            DadosDiarioInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<Object> handlerDataInvalidaException(DataInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DiarioJaExisteException.class)
    public ResponseEntity<Object> handlerDiarioJaExiste(
            DiarioJaExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("Arquivo excede o tamanho máximo permitido. Tamanho máximo: " + tamanhoMaximo);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex) {

        var erros = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    Map<String, String> erro = new HashMap<>();
                    erro.put("campo", violation.getPropertyPath().toString());
                    erro.put("mensagem", violation.getMessage());
                    return erro;
                })
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleEnumError(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException e) {
            return handleInvalidFormatException(e);
        }

        return ResponseEntity.badRequest().body("Erro ao interpretar JSON: ");
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex) {
        if (ex.getTargetType().isEnum()) {
            Object[] valores = ex.getTargetType().getEnumConstants();
            String mensagem = "Valor inválido. Valores permitidos: " + Arrays.toString(valores);
            return ResponseEntity.badRequest().body(mensagem);
        }

        if (ex.getTargetType().equals(java.time.LocalDate.class) ||
                ex.getTargetType().equals(java.time.LocalDateTime.class)) {

            String campo = ex.getPath().isEmpty() ? "data" : ex.getPath().get(0).getFieldName();
            String mensagem = String
                    .format("O campo '%s' está com um formato de data inválido. Use o padrão DD/MM/YYYY.", campo);
            return ResponseEntity.badRequest().body(mensagem);
        }

        return ResponseEntity.badRequest().body("Erro na formatação dos dados enviados: " + ex.getOriginalMessage());
    }
}
