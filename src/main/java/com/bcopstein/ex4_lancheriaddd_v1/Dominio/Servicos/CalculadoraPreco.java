package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Service
public class CalculadoraPreco {

    private final DescontoPolicy politicaCorrente;
    private final IImpostoService impostoService;

    @Autowired
    public CalculadoraPreco(DescontoPolicy politicaCorrente, IImpostoService impostoService) {
        this.politicaCorrente = politicaCorrente;
        this.impostoService = impostoService;
    }

    public ResultadoCalculo calcular(List<ItemPedido> itens) {
        return calcular(itens, null);
    }

    public ResultadoCalculo calcular(List<ItemPedido> itens, String clienteCpf) {
        double valor = itens.stream()
            .mapToDouble(i -> (double) i.getItem().getPreco() * i.getQuantidade())
            .sum();
        double impostos = impostoService.calcular(valor);
        double desconto = politicaCorrente.seAplica(clienteCpf) ? politicaCorrente.calcular(itens) : 0.0;
        double valorCobrado = valor + impostos - desconto;
        return new ResultadoCalculo(valor, impostos, desconto, valorCobrado);
    }

    public record ResultadoCalculo(double valor, double impostos, double desconto, double valorCobrado) {}
}
