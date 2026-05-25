package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

@Service
public class EntregaService implements IEntregaService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;
    private final ScheduledExecutorService scheduler;

    public EntregaService(PedidoRepository pedidoRepository,
                          PedidoStatusRepository statusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void iniciarEntrega(Pedido pedido) {
        pedido.setStatus(Pedido.Status.TRANSPORTE);
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.TRANSPORTE, LocalDateTime.now(), "entregador"));
        scheduler.schedule(() -> finalizar(pedido), 10, TimeUnit.SECONDS);
    }

    private void finalizar(Pedido pedido) {
        pedido.setStatus(Pedido.Status.ENTREGUE);
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.ENTREGUE, LocalDateTime.now(), "entregador"));
    }
}
