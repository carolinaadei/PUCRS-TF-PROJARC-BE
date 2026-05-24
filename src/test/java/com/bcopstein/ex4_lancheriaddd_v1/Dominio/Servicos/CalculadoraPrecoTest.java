package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CalculadoraPreco.ResultadoCalculo;

class CalculadoraPrecoTest {

    private static final double DELTA = 0.001;

    private Produto produto(int precoCentavos) {
        Receita receita = new Receita(1L, "receita", List.of(new Ingrediente(1L, "ing")));
        return new Produto(1L, "Produto", receita, precoCentavos);
    }

    // ── Sem políticas de desconto ─────────────────────────────────────────────

    @Test
    @DisplayName("Valor base = soma dos itens")
    void semDesconto_calculaValorBase() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of());
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 2)); // R$10 x2

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(2000.0, r.valor(), DELTA);
    }

    @Test
    @DisplayName("Imposto = 10% do valor base")
    void semDesconto_calculaImpostos10Porcento() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of());
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(100.0, r.impostos(), DELTA);
    }

    @Test
    @DisplayName("Desconto = 0 sem políticas")
    void semDesconto_descontoZero() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of());
        List<ItemPedido> itens = List.of(new ItemPedido(produto(5000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(0.0, r.desconto(), DELTA);
    }

    @Test
    @DisplayName("valorCobrado = valor + impostos sem desconto")
    void semDesconto_valorCobrado() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of());
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(1100.0, r.valorCobrado(), DELTA); // 1000 + 100
    }

    @Test
    @DisplayName("Múltiplos itens somam corretamente")
    void semDesconto_multiplosItens() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of());
        Produto p1 = produto(3000);
        Produto p2 = produto(2000);
        List<ItemPedido> itens = List.of(
            new ItemPedido(p1, 2),  // 6000
            new ItemPedido(p2, 3)   // 6000
        );

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(12000.0, r.valor(), DELTA);
        assertEquals(1200.0, r.impostos(), DELTA);
        assertEquals(13200.0, r.valorCobrado(), DELTA);
    }

    // ── Com DescontoClienteFrequente (7%) ─────────────────────────────────────

    @Test
    @DisplayName("Desconto cliente frequente = 7% do valor base")
    void clienteFrequente_desconto7Porcento() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of(new DescontoClienteFrequente()));
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(70.0, r.desconto(), DELTA); // 7% de 1000
    }

    @Test
    @DisplayName("valorCobrado com desconto frequente = valor + impostos - desconto")
    void clienteFrequente_valorCobradoComDesconto() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of(new DescontoClienteFrequente()));
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        // valor=1000, impostos=100, desconto=70 → valorCobrado=1030
        assertEquals(1030.0, r.valorCobrado(), DELTA);
    }

    // ── SemDesconto ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("SemDesconto sempre retorna zero")
    void semDescontoPolicy_sempreZero() {
        SemDesconto policy = new SemDesconto();
        List<ItemPedido> itens = List.of(new ItemPedido(produto(9999), 10));

        assertEquals(0.0, policy.calcular(itens), DELTA);
    }

    // ── null policies (construtor defensivo) ──────────────────────────────────

    @Test
    @DisplayName("CalculadoraPreco aceita lista nula e trata como sem desconto")
    void nullPolicies_trataComoSemDesconto() {
        CalculadoraPreco calc = new CalculadoraPreco(null);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(0.0, r.desconto(), DELTA);
        assertEquals(1100.0, r.valorCobrado(), DELTA);
    }
}
