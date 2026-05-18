package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

public interface IPaymentService {
    /**
     * Processa o pagamento de um pedido.
     * Retorna true se o pagamento foi aprovado, false caso contrário.
     */
    boolean processPayment(long pedidoId, double valor);
}