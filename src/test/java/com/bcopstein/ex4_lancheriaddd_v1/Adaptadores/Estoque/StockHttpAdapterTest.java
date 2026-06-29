package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PorcaoIngrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * Exercita o contrato síncrono pizzaria -> estoque-service descrito em
 * docs/contracts/estoque-service-contract.md, sem precisar do estoque-service
 * real rodando: o round-trip HTTP é simulado com MockRestServiceServer,
 * validando o JSON exato enviado/recebido.
 */
class StockHttpAdapterTest {

    private static final String ESTOQUE_URL = "http://estoque-service:8001";

    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private StockHttpAdapter adapter;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
        adapter = new StockHttpAdapter(restTemplate);
        ReflectionTestUtils.setField(adapter, "estoqueServiceUrl", ESTOQUE_URL);
    }

    private ItemPedido itemComReceita(int quantidadePedida) {
        Ingrediente disco = new Ingrediente(1L, "Disco de pizza");
        Ingrediente queijo = new Ingrediente(3L, "Porcao de mussarela");
        Receita receita = new Receita(1L, "Pizza calabresa", List.of(disco, queijo));
        receita.setPorcoes(List.of(
            new PorcaoIngrediente(disco, 1),
            new PorcaoIngrediente(queijo, 2)));
        Produto produto = new Produto(1L, "Pizza calabresa", receita, 5500);
        return new ItemPedido(produto, quantidadePedida);
    }

    @Test
    @DisplayName("Envia ingredientes com porção * quantidade pedida e retorna disponivel=true quando o estoque confirma a baixa")
    void verifyItem_estoqueSuficiente_retornaTrue() {
        server.expect(requestTo(ESTOQUE_URL + "/estoque/verificar"))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andExpect(content().json("""
                {"ingredientes":[{"ingredienteId":1,"quantidade":2},{"ingredienteId":3,"quantidade":4}]}
                """))
            .andRespond(withSuccess("""
                {"disponivel":true,"ingredientesIndisponiveis":[]}
                """, MediaType.APPLICATION_JSON));

        boolean disponivel = adapter.verifyItem(itemComReceita(2));

        assertTrue(disponivel);
        server.verify();
    }

    @Test
    @DisplayName("Retorna disponivel=false e não lança exceção quando o estoque informa ingredientes em falta")
    void verifyItem_estoqueInsuficiente_retornaFalse() {
        server.expect(requestTo(ESTOQUE_URL + "/estoque/verificar"))
            .andRespond(withSuccess("""
                {"disponivel":false,"ingredientesIndisponiveis":[3]}
                """, MediaType.APPLICATION_JSON));

        boolean disponivel = adapter.verifyItem(itemComReceita(1));

        assertFalse(disponivel);
        server.verify();
    }

    @Test
    @DisplayName("Propaga falha como RuntimeException quando o estoque-service está fora do ar")
    void verifyItem_estoqueServiceIndisponivel_lancaExcecao() {
        server.expect(requestTo(ESTOQUE_URL + "/estoque/verificar"))
            .andRespond(withServerError());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> adapter.verifyItem(itemComReceita(1)));
        assertTrue(ex.getMessage().contains("Serviço de estoque indisponível"));
    }

    @Test
    @DisplayName("Assume disponível e não chama o estoque-service quando o produto não tem receita")
    void verifyItem_semReceita_assumeDisponivelSemChamarEstoque() {
        Produto produtoSemReceita = new Produto(2L, "Refrigerante", null, 800);
        ItemPedido item = new ItemPedido(produtoSemReceita, 1);

        boolean disponivel = adapter.verifyItem(item);

        assertTrue(disponivel);
        server.verify();
    }
}
