package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.AcessoNaoAutorizadoException;

/**
 * Handler global de exceções.
 *
 * Atualizado em relação à versão original para mapear
 * {@link AcessoNaoAutorizadoException} para HTTP 403 Forbidden,
 * mantendo RuntimeException genérica como 400 Bad Request.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AcessoNaoAutorizadoException.class)
    public ResponseEntity<Map<String, String>> handleAcessoNaoAutorizado(
            AcessoNaoAutorizadoException ex) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("erro", ex.getMessage()));
    }
}