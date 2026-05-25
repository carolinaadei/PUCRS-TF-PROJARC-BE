package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class FakePaymentService implements IPaymentService {

    @Override
    public boolean processPayment(long pedidoId, double valor) {
        /*
        Fake payment service — always approves payment.
        In a real implementation, this would integrate with a payment gateway.
        
        Returns:
            true: payment approved
            false: payment rejected
        */
        System.out.println("[FAKE PAYMENT] Processing payment for order: " + pedidoId + " value: " + valor);
        System.out.println("[FAKE PAYMENT] Payment approved!");
        return true;
    }
}