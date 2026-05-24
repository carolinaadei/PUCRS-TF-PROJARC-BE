package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

/**
 * Exceção de domínio lançada quando um cliente tenta acessar
 * um pedido que não lhe pertence.
 *
 * Separada de IllegalArgumentException para permitir mapeamento
 * para HTTP 403 Forbidden no GlobalExceptionHandler.
 */
public class AcessoNaoAutorizadoException extends RuntimeException {

    public AcessoNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}