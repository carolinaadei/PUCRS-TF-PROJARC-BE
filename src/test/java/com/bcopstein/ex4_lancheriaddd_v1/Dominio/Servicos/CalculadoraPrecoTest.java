package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CalculadoraPreco.ResultadoCalculo;

class CalculadoraPrecoTest {

    private static final double DELTA = 0.001;

    private IImpostoService impostoSimples;
    private PedidoRepository mockRepo;

    @BeforeEach
    void setUp() {
        impostoSimples = new ImpostoSimples();
        mockRepo = mock(PedidoRepository.class);
    }

    private Produto produto(int precoCentavos) {
        Receita receita = new Receita(1L, "receita", List.of(new Ingrediente(1L, "ing")));
        return new Produto(1L, "Produto", receita, precoCentavos);
    }

    // ── Sem políticas de desconto ─────────────────────────────────────────────

    @Test
    @DisplayName("Valor base = soma dos itens")
    void semDesconto_calculaValorBase() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of(), impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 2));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(2000.0, r.valor(), DELTA);
    }

    @Test
    @DisplayName("Imposto = 10% do valor base")
    void semDesconto_calculaImpostos10Porcento() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of(), impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(100.0, r.impostos(), DELTA);
    }

    @Test
    @DisplayName("Desconto = 0 sem políticas")
    void semDesconto_descontoZero() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of(), impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(5000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(0.0, r.desconto(), DELTA);
    }

    @Test
    @DisplayName("valorCobrado = valor + impostos sem desconto")
    void semDesconto_valorCobrado() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of(), impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(1100.0, r.valorCobrado(), DELTA);
    }

    @Test
    @DisplayName("Múltiplos itens somam corretamente")
    void semDesconto_multiplosItens() {
        CalculadoraPreco calc = new CalculadoraPreco(List.of(), impostoSimples);
        Produto p1 = produto(3000);
        Produto p2 = produto(2000);
        List<ItemPedido> itens = List.of(
            new ItemPedido(p1, 2),
            new ItemPedido(p2, 3)
        );

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(12000.0, r.valor(), DELTA);
        assertEquals(1200.0, r.impostos(), DELTA);
        assertEquals(13200.0, r.valorCobrado(), DELTA);
    }

    // ── Com DescontoClienteFrequente (7%) — cliente frequente ─────────────────

    @Test
    @DisplayName("Desconto cliente frequente = 7% do valor base quando elegível")
    void clienteFrequente_desconto7Porcento() {
        when(mockRepo.contarPedidosRecentes(anyString(), any())).thenReturn(4L);
        DescontoClienteFrequente policy = new DescontoClienteFrequente(mockRepo);
        CalculadoraPreco calc = new CalculadoraPreco(List.of(policy), impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens, "cpf-frequente");

        assertEquals(70.0, r.desconto(), DELTA);
    }

    @Test
    @DisplayName("valorCobrado com desconto frequente = valor + impostos - desconto")
    void clienteFrequente_valorCobradoComDesconto() {
        when(mockRepo.contarPedidosRecentes(anyString(), any())).thenReturn(4L);
        DescontoClienteFrequente policy = new DescontoClienteFrequente(mockRepo);
        CalculadoraPreco calc = new CalculadoraPreco(List.of(policy), impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens, "cpf-frequente");

        assertEquals(1030.0, r.valorCobrado(), DELTA);
    }

    @Test
    @DisplayName("Desconto não aplicado quando cliente não é frequente")
    void clienteNaoFrequente_semDesconto() {
        when(mockRepo.contarPedidosRecentes(anyString(), any())).thenReturn(2L);
        DescontoClienteFrequente policy = new DescontoClienteFrequente(mockRepo);
        CalculadoraPreco calc = new CalculadoraPreco(List.of(policy), impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens, "cpf-novo");

        assertEquals(0.0, r.desconto(), DELTA);
        assertEquals(1100.0, r.valorCobrado(), DELTA);
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
        CalculadoraPreco calc = new CalculadoraPreco(null, impostoSimples);
        List<ItemPedido> itens = List.of(new ItemPedido(produto(1000), 1));

        ResultadoCalculo r = calc.calcular(itens);

        assertEquals(0.0, r.desconto(), DELTA);
        assertEquals(1100.0, r.valorCobrado(), DELTA);
    }
}
