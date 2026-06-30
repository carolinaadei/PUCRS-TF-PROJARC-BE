package com.bcopstein.deliveryservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PizzariaClient {

    private static final Logger log = LoggerFactory.getLogger(PizzariaClient.class);

    private final RestTemplate restTemplate;
    private final String pizzariaUrl;

    public PizzariaClient(RestTemplate restTemplate,
                          @Value("${pizzaria.service.url:http://localhost:8080}") String pizzariaUrl) {
        this.restTemplate = restTemplate;
        this.pizzariaUrl = pizzariaUrl;
    }

    public void confirmarEntrega(long pedidoId) {
        String url = pizzariaUrl + "/pedidos/" + pedidoId + "/entregue";
        log.info("Confirmando entrega do pedido {} em {}", pedidoId, url);
        restTemplate.postForEntity(url, null, Void.class);
    }
}
