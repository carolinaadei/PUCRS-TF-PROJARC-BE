package com.pizzaria.entrega.service;

import com.pizzaria.entrega.config.RabbitMQConfig;
import com.pizzaria.entrega.dto.PedidoEntregaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EntregaConsumer {

    private static final Logger log = LoggerFactory.getLogger(EntregaConsumer.class);

    private final RestTemplate restTemplate;

    @Value("${pizzaria.service.url:http://pizzaria:8080}")
    private String pizzariaUrl;

    public EntregaConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receberPedido(PedidoEntregaDTO dto) {
        log.info("[ENTREGA] Pedido {} recebido. Endereço: {}", dto.pedidoId(), dto.enderecoEntrega());

        simularEntrega(dto.pedidoId());

        notificarEntregue(dto.pedidoId());
    }

    private void simularEntrega(Long pedidoId) {
        log.info("[ENTREGA] Iniciando entrega do pedido {}...", pedidoId);
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[ENTREGA] Pedido {} entregue ao cliente!", pedidoId);
    }

    private void notificarEntregue(Long pedidoId) {
        String url = pizzariaUrl + "/interno/pedidos/" + pedidoId + "/entregue";
        try {
            restTemplate.postForObject(url, null, Void.class);
            log.info("[ENTREGA] Monolito notificado: pedido {} marcado como ENTREGUE.", pedidoId);
        } catch (Exception e) {
            log.error("[ENTREGA] Falha ao notificar monolito para pedido {}: {}", pedidoId, e.getMessage());
        }
    }
}
