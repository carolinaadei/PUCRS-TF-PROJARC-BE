package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Service
public class CalculadoraPreco {

    private static final double TAXA_IMPOSTO = 0.10;

    private final List<DescontoPolicy> policies;

    @Autowired(required = false)
    public CalculadoraPreco(List<DescontoPolicy> policies) {
        this.policies = policies != null ? policies : List.of();
    }

    public ResultadoCalculo calcular(List<ItemPedido> itens) {
        double valor = itens.stream()
            .mapToDouble(i -> (double) i.getItem().getPreco() * i.getQuantidade())
            .sum();
        double impostos = valor * TAXA_IMPOSTO;
        double desconto = policies.stream()
            .mapToDouble(p -> p.calcular(itens))
            .sum();
        double valorCobrado = valor + impostos - desconto;
        return new ResultadoCalculo(valor, impostos, desconto, valorCobrado);
    }

    public record ResultadoCalculo(double valor, double impostos, double desconto, double valorCobrado) {}
}
