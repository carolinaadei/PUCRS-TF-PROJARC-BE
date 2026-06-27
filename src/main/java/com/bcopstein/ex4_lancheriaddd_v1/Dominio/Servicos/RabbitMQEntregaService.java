package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;
import com.bcopstein.ex4_lancheriaddd_v1.Infra.Mensageria.PedidoEntregaDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Infra.Mensageria.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Primary
@Service
public class RabbitMQEntregaService implements IEntregaService {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEntregaService.class);

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEntregaService(PedidoRepository pedidoRepository,
                                   PedidoStatusRepository statusRepository,
                                   RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void iniciarEntrega(Pedido pedido) {
        pedido.setStatus(Pedido.Status.TRANSPORTE);
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.TRANSPORTE, LocalDateTime.now(), "entregador"));

        PedidoEntregaDTO dto = new PedidoEntregaDTO(pedido.getId(), pedido.getEnderecoEntrega());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, dto);

        log.info("Pedido {} enviado para fila de entrega.", pedido.getId());
    }
}
