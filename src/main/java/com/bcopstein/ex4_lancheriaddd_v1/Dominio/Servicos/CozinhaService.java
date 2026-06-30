package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

@Primary
@Service
public class CozinhaService implements ICozinhaService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;
    private final IEntregaService entregaService;
    private final Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private final ScheduledExecutorService scheduler;

    public CozinhaService(PedidoRepository pedidoRepository,
                          PedidoStatusRepository statusRepository,
                          IEntregaService entregaService) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        this.entregaService = entregaService;
        this.filaEntrada = new LinkedBlockingQueue<>();
        this.emPreparacao = null;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmPreparacao(Pedido pedido) {
        pedido.setStatus(Pedido.Status.PREPARACAO);
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.PREPARACAO, LocalDateTime.now(), "cozinha"));
        emPreparacao = pedido;
        scheduler.schedule(this::pedidoPronto, 5, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void chegadaDePedido(Pedido p) {
        p.setStatus(Pedido.Status.AGUARDANDO);
        pedidoRepository.salvar(p);
        statusRepository.registrar(new PedidoStatusHistorico(
            p.getId(), Pedido.Status.AGUARDANDO, LocalDateTime.now(), "cozinha"));
        filaEntrada.add(p);
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        }
    }

    @Override
    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO);
        pedidoRepository.salvar(emPreparacao);
        statusRepository.registrar(new PedidoStatusHistorico(
            emPreparacao.getId(), Pedido.Status.PRONTO, LocalDateTime.now(), "cozinha"));

        Pedido pronto = emPreparacao;
        emPreparacao = null;

        scheduler.schedule(() -> entregaService.iniciarEntrega(pronto), 1, TimeUnit.SECONDS);

        if (!filaEntrada.isEmpty()) {
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}
