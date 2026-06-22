package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Component
public class DescontoClienteFrequente implements DescontoPolicy {

    private static final double TAXA = 0.07;
    private static final int MINIMO_PEDIDOS = 3;
    private static final int JANELA_DIAS = 20;

    private final PedidoRepository pedidoRepository;

    public DescontoClienteFrequente(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public boolean seAplica(String clienteCpf) {
        if (clienteCpf == null || clienteCpf.isBlank()) return false;
        LocalDateTime desde = LocalDateTime.now().minusDays(JANELA_DIAS);
        return pedidoRepository.contarPedidosRecentes(clienteCpf, desde) > MINIMO_PEDIDOS;
    }

    @Override
    public double calcular(List<ItemPedido> itens) {
        double subtotal = itens.stream()
            .mapToDouble(i -> (double) i.getItem().getPreco() * i.getQuantidade())
            .sum();
        return subtotal * TAXA;
    }
}
