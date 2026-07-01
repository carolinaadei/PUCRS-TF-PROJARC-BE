package com.pizzaria.entrega.service;

import com.pizzaria.entrega.config.RabbitMQConfig;
import com.pizzaria.entrega.dto.PedidoEntregaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EntregaConsumer {

    private static final Logger log = LoggerFactory.getLogger(EntregaConsumer.class);

    private final RestTemplate restTemplate;

    @Value("${pizzaria.service.url:http://pizzaria:8080}")
    private String pizzariaUrl;

    @Value("${internal.secret:pizzaria-delivery-secret}")
    private String internalSecret;

    public EntregaConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receberPedido(PedidoEntregaDTO dto) {
        log.info("[ENTREGA] Pedido {} recebido. Endereço: {}", dto.pedidoId(), dto.enderecoEntrega());

        notificarTransporte(dto.pedidoId());
    }

    private void notificarTransporte(Long pedidoId) {
        String url = pizzariaUrl + "/interno/pedidos/" + pedidoId + "/transporte";
        try {
            restTemplate.exchange(url, HttpMethod.POST, requestComSecret(), Void.class);
            log.info("[ENTREGA] Pedido {} marcado como TRANSPORTE.", pedidoId);
        } catch (Exception e) {
            log.error("[ENTREGA] Falha ao marcar TRANSPORTE para pedido {}: {}", pedidoId, e.getMessage());
        }
    }

    private HttpEntity<Void> requestComSecret() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Secret", internalSecret);
        return new HttpEntity<>(headers);
    }
}
