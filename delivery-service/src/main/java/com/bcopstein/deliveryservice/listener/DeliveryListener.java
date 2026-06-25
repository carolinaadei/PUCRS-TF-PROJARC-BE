package com.bcopstein.deliveryservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.bcopstein.deliveryservice.config.RabbitMQConfig;
import com.bcopstein.deliveryservice.dto.DeliveryMessage;
import com.bcopstein.deliveryservice.service.DeliverySimulationService;

@Component
public class DeliveryListener {

    private static final Logger log = LoggerFactory.getLogger(DeliveryListener.class);

    private final DeliverySimulationService simulationService;

    public DeliveryListener(DeliverySimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @RabbitListener(queues = RabbitMQConfig.DELIVERY_QUEUE)
    public void onDeliveryRequest(DeliveryMessage message) {
        log.info("Mensagem de entrega recebida: pedidoId={}, endereco={}",
                message.pedidoId(), message.enderecoEntrega());
        simulationService.simularEntrega(message);
    }
}
