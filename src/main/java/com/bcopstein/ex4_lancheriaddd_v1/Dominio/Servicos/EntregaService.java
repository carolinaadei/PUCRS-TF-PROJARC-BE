package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Mensageria.DeliveryMessage;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Mensageria.RabbitMQConfig;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

@Service
public class EntregaService implements IEntregaService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;
    private final RabbitTemplate rabbitTemplate;

    public EntregaService(PedidoRepository pedidoRepository,
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

        DeliveryMessage message = new DeliveryMessage(pedido.getId(), pedido.getEnderecoEntrega());
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.DELIVERY_EXCHANGE,
            RabbitMQConfig.DELIVERY_ROUTING_KEY,
            message);
    }
}
