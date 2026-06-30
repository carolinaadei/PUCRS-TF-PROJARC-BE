package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PorcaoIngrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IStockService.VerificacaoResultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

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

    private List<ItemPedido> itensComReceita(int quantidadePedida) {
        Ingrediente disco = new Ingrediente(1L, "Disco de pizza");
        Ingrediente queijo = new Ingrediente(3L, "Porcao de mussarela");
        Receita receita = new Receita(1L, "Pizza calabresa", List.of(disco, queijo));
        receita.setPorcoes(List.of(
            new PorcaoIngrediente(disco, 1),
            new PorcaoIngrediente(queijo, 2)));
        Produto produto = new Produto(1L, "Pizza calabresa", receita, 5500);
        return List.of(new ItemPedido(produto, quantidadePedida));
    }

    @Test
    @DisplayName("Agrega ingredientes de todos os itens em uma única chamada e retorna disponivel=true")
    void verificarDisponibilidade_estoqueSuficiente_retornaTrue() {
        server.expect(requestTo(ESTOQUE_URL + "/estoque/verificar"))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andRespond(withSuccess("""
                {"disponivel":true,"ingredientesIndisponiveis":[]}
                """, MediaType.APPLICATION_JSON));

        VerificacaoResultado resultado = adapter.verificarDisponibilidade(itensComReceita(2));

        assertTrue(resultado.disponivel());
        assertTrue(resultado.ingredientesIndisponiveis().isEmpty());
        server.verify();
    }

    @Test
    @DisplayName("Retorna disponivel=false com lista de ingredientes em falta")
    void verificarDisponibilidade_estoqueInsuficiente_retornaFalse() {
        server.expect(requestTo(ESTOQUE_URL + "/estoque/verificar"))
            .andRespond(withSuccess("""
                {"disponivel":false,"ingredientesIndisponiveis":[3]}
                """, MediaType.APPLICATION_JSON));

        VerificacaoResultado resultado = adapter.verificarDisponibilidade(itensComReceita(1));

        assertFalse(resultado.disponivel());
        assertEquals(List.of(3L), resultado.ingredientesIndisponiveis());
        server.verify();
    }

    @Test
    @DisplayName("Propaga falha como RuntimeException quando o estoque-service está fora do ar")
    void verificarDisponibilidade_estoqueServiceIndisponivel_lancaExcecao() {
        server.expect(requestTo(ESTOQUE_URL + "/estoque/verificar"))
            .andRespond(withServerError());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> adapter.verificarDisponibilidade(itensComReceita(1)));
        assertTrue(ex.getMessage().contains("Serviço de estoque indisponível"));
    }

    @Test
    @DisplayName("Assume disponível e não chama o estoque-service quando nenhum item tem receita")
    void verificarDisponibilidade_semReceita_assumeDisponivelSemChamarEstoque() {
        Produto produtoSemReceita = new Produto(2L, "Refrigerante", null, 800);
        List<ItemPedido> itens = List.of(new ItemPedido(produtoSemReceita, 1));

        VerificacaoResultado resultado = adapter.verificarDisponibilidade(itens);

        assertTrue(resultado.disponivel());
        server.verify();
    }

    @Test
    @DisplayName("Agrega quantidades do mesmo ingrediente de itens diferentes em uma única entrada")
    void verificarDisponibilidade_doisItensMesmoIngrediente_agregaQuantidades() {
        Ingrediente disco = new Ingrediente(1L, "Disco de pizza");
        Receita receita = new Receita(1L, "Pizza", List.of(disco));
        receita.setPorcoes(List.of(new PorcaoIngrediente(disco, 1)));
        Produto produto = new Produto(1L, "Pizza", receita, 5000);

        List<ItemPedido> itens = List.of(
            new ItemPedido(produto, 2),
            new ItemPedido(produto, 3));

        server.expect(requestTo(ESTOQUE_URL + "/estoque/verificar"))
            .andExpect(method(org.springframework.http.HttpMethod.POST))
            .andExpect(content().json("""
                {"ingredientes":[{"ingredienteId":1,"quantidade":5}]}
                """))
            .andRespond(withSuccess("""
                {"disponivel":true,"ingredientesIndisponiveis":[]}
                """, MediaType.APPLICATION_JSON));

        VerificacaoResultado resultado = adapter.verificarDisponibilidade(itens);

        assertTrue(resultado.disponivel());
        server.verify();
    }
}
