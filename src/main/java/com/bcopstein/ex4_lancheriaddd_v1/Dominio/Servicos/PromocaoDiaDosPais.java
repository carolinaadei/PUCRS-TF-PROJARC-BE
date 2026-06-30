package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Component
public class PromocaoDiaDosPais implements DescontoPolicy {

    public static final String CODIGO = "PromocaoDiaDosPais";
    private static final double TAXA = 0.10;

    @Override
    public String getCodigo() {
        return CODIGO;
    }

    @Override
    public double calcular(List<ItemPedido> itens) {
        double subtotal = itens.stream()
            .mapToDouble(i -> (double) i.getItem().getPreco() * i.getQuantidade())
            .sum();
        return subtotal * TAXA;
    }
}
