package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Testes de integração para GET /pedidos/{id}/status.
 *
 * Usa o banco H2 em memória carregado com schema.sql + data.sql, sem mocks.
 * Dados disponíveis (data.sql):
 *   Pedido 1 — cpf 9001, status APROVADO,  2 entradas no histórico
 *   Pedido 2 — cpf 9002, status PAGO,      3 entradas no histórico
 *   Pedido 3 — cpf 9001, status ENTREGUE,  8 entradas no histórico
 */
@SpringBootTest
@AutoConfigureMockMvc
class AcompanhamentoPedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── Caminho feliz ─────────────────────────────────────────────────────────

    @Test
    void deveRetornar200_comStatusEHistorico_quandoPedidoECpfCorretos() throws Exception {
        mockMvc.perform(get("/pedidos/1/status").param("cpf", "9001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido", is(1)))
                .andExpect(jsonPath("$.statusAtual", is("APROVADO")))
                .andExpect(jsonPath("$.descricaoStatus", is("Pedido aprovado e aguardando pagamento")))
                .andExpect(jsonPath("$.enderecoEntrega", is("Rua das Flores, 100")))
                .andExpect(jsonPath("$.historico", hasSize(2)))
                .andExpect(jsonPath("$.historico[0].status", is("NOVO")))
                .andExpect(jsonPath("$.historico[0].responsavel", is("cliente")))
                .andExpect(jsonPath("$.historico[1].status", is("APROVADO")))
                .andExpect(jsonPath("$.historico[1].responsavel", is("sistema")));
    }

    @Test
    void deveRetornar200_comHistoricoCompleto_parapedidoEntregue() throws Exception {
        mockMvc.perform(get("/pedidos/3/status").param("cpf", "9001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido", is(3)))
                .andExpect(jsonPath("$.statusAtual", is("ENTREGUE")))
                .andExpect(jsonPath("$.historico", hasSize(8)))
                .andExpect(jsonPath("$.historico[0].status", is("NOVO")))
                .andExpect(jsonPath("$.historico[7].status", is("ENTREGUE")));
    }

    // ── Acesso não autorizado ─────────────────────────────────────────────────

    @Test
    void deveRetornar403_quandoCpfNaoPertenceAoPedido() throws Exception {
        mockMvc.perform(get("/pedidos/1/status").param("cpf", "9002"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void deveRetornar403_quandoPedidoDe9002AcessadoPor9001() throws Exception {
        mockMvc.perform(get("/pedidos/2/status").param("cpf", "9001"))
                .andExpect(status().isForbidden());
    }

    // ── Pedido inexistente ────────────────────────────────────────────────────

    @Test
    void deveRetornar400_quandoPedidoNaoExiste() throws Exception {
        mockMvc.perform(get("/pedidos/999/status").param("cpf", "9001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    // ── CPF ausente / vazio ───────────────────────────────────────────────────

    @Test
    void deveRetornar400_quandoCpfNaoInformado() throws Exception {
        mockMvc.perform(get("/pedidos/1/status"))
                .andExpect(status().isBadRequest());
    }
}
