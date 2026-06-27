package com.bcopstein.deliveryservice.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bcopstein.deliveryservice.client.PizzariaClient;
import com.bcopstein.deliveryservice.dto.DeliveryMessage;

@Service
public class DeliverySimulationService {

    private static final Logger log = LoggerFactory.getLogger(DeliverySimulationService.class);
    private static final int DELIVERY_DELAY_SECONDS = 10;

    private final PizzariaClient pizzariaClient;

    public DeliverySimulationService(PizzariaClient pizzariaClient) {
        this.pizzariaClient = pizzariaClient;
    }

    public void simularEntrega(DeliveryMessage message) {
        log.info("Iniciando simulação de entrega para pedido {} em {}",
                message.pedidoId(), message.enderecoEntrega());

        try {
            TimeUnit.SECONDS.sleep(DELIVERY_DELAY_SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Entrega do pedido {} interrompida", message.pedidoId());
            return;
        }

        log.info("Entrega do pedido {} concluída. Notificando pizzaria.", message.pedidoId());
        pizzariaClient.confirmarEntrega(message.pedidoId());
    }
}
