package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Infra.Mensageria.PedidoEntregaDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Infra.Mensageria.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RabbitMQEntregaService implements IEntregaService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEntregaService.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEntregaService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void iniciarEntrega(Pedido pedido) {
        PedidoEntregaDTO dto = new PedidoEntregaDTO(pedido.getId(), pedido.getEnderecoEntrega());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, dto);
        log.info("Pedido {} publicado na fila de entrega.", pedido.getId());
    }
}
