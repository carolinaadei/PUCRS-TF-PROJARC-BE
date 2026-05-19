package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusHistoricoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

@Primary
@Service
public class CozinhaService implements ICozinhaService {
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;
    private ScheduledExecutorService scheduler;

    private PedidoRepository pedidoRepository;
    private PedidoStatusHistoricoRepository historicoRepository;

    @Autowired
    public CozinhaService(PedidoRepository pedidoRepository,
                          PedidoStatusHistoricoRepository historicoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.historicoRepository = historicoRepository;
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmPreparacao(Pedido pedido) {
        pedido.setStatus(Pedido.Status.PREPARACAO);
        pedidoRepository.salvar(pedido);
        historicoRepository.salvar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.PREPARACAO, LocalDateTime.now(), "cozinha"
        ));
        emPreparacao = pedido;
        System.out.println("Pedido em preparacao: " + pedido.getId());
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void chegadaDePedido(Pedido p) {
        p.setStatus(Pedido.Status.AGUARDANDO);
        pedidoRepository.salvar(p);
        historicoRepository.salvar(new PedidoStatusHistorico(
            p.getId(), Pedido.Status.AGUARDANDO, LocalDateTime.now(), "cozinha"
        ));
        filaEntrada.add(p);
        System.out.println("Pedido na fila de entrada: " + p.getId());
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        }
    }

    @Override
    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO);
        pedidoRepository.salvar(emPreparacao);
        historicoRepository.salvar(new PedidoStatusHistorico(
            emPreparacao.getId(), Pedido.Status.PRONTO, LocalDateTime.now(), "cozinha"
        ));
        filaSaida.add(emPreparacao);
        System.out.println("Pedido na fila de saida: " + emPreparacao.getId());
        emPreparacao = null;
        if (!filaEntrada.isEmpty()) {
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}