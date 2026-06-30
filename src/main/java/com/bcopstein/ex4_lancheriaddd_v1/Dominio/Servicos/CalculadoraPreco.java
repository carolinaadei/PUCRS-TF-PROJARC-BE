package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;


import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Service
public class CalculadoraPreco {

    private final ConfiguracaoDesconto configuracaoDesconto;
    private final IImpostoService impostoService;

    public CalculadoraPreco(ConfiguracaoDesconto configuracaoDesconto, IImpostoService impostoService) {
        this.configuracaoDesconto = configuracaoDesconto;
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
        DescontoPolicy politica = configuracaoDesconto.getPoliticaCorrente();
        double desconto = politica.seAplica(clienteCpf) ? politica.calcular(itens) : 0.0;
        double valorCobrado = valor + impostos - desconto;
        return new ResultadoCalculo(valor, impostos, desconto, valorCobrado);
    }

    public record ResultadoCalculo(double valor, double impostos, double desconto, double valorCobrado) {}
}
