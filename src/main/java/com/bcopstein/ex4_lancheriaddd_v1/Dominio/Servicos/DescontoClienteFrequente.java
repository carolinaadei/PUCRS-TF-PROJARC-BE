package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

public class DescontoClienteFrequente implements DescontoPolicy {

    private static final double TAXA = 0.07;

    @Override
    public double calcular(List<ItemPedido> itens) {
        double subtotal = itens.stream()
            .mapToDouble(i -> (double) i.getItem().getPreco() * i.getQuantidade())
            .sum();
        return subtotal * TAXA;
    }
}
