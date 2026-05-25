package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

@Service
public class CozinhaService implements ICozinhaService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;
    private ScheduledExecutorService scheduler;

    public CozinhaService(PedidoRepository pedidoRepository,
                          PedidoStatusRepository statusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        filaEntrada = new LinkedBlockingQueue<>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmPreparacao(Pedido pedido) {
        pedido.setStatus(Pedido.Status.PREPARACAO);
        emPreparacao = pedido;
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.PREPARACAO, LocalDateTime.now(), "cozinha"));
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
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
        filaSaida.add(pronto);
        emPreparacao = null;

        scheduler.schedule(() -> iniciarTransporte(pronto), 1, TimeUnit.SECONDS);

        if (!filaEntrada.isEmpty()) {
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }

    public Queue<Pedido> getFilaSaida() {
        return filaSaida;
    }

    private void iniciarTransporte(Pedido pedido) {
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