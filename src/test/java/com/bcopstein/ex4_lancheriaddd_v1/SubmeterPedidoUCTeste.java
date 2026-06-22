package com.bcopstein.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

/**
 * Testes unitários para {@link SubmeterPedidoUC}.
 *
 * Verifica o mapeamento Request → domínio → Response,
 * sem acionar o banco de dados.
 */
@ExtendWith(MockitoExtension.class)
class SubmeterPedidoUCTest {

    @Mock
    private PedidoService pedidoService;

    @Mock
    private ProdutosRepository produtosRepository;

    @InjectMocks
    private SubmeterPedidoUC submeterPedidoUC;

    private Produto produtoCalabresa;
    private Cliente clienteHuguinho;

    @BeforeEach
    void setUp() {
        Receita receita = new Receita(1L, "Pizza calabresa",
                                      List.of(new Ingrediente(1L, "Disco de pizza")));
        produtoCalabresa = new Produto(1L, "Pizza calabresa", receita, 5500);
        clienteHuguinho = new Cliente(null, "Huguinho Pato", "9001", "51985744566",
                                      "Rua das Flores, 100", "huguinho@email.com", null);
    }

    @Test
    @DisplayName("Deve retornar response com status NOVO e ID do pedido criado")
    void run_requestValido_retornaResponseComIdEStatusNovo() {
        // Arrange
        SubmeterPedidoRequest request = new SubmeterPedidoRequest(
            "9001",
            "Rua das Flores, 100",
            List.of(new ItemPedidoRequest(1L, 2))
        );

        when(produtosRepository.recuperaProdutoPorid(1L)).thenReturn(produtoCalabresa);

        Pedido pedidoCriado = new Pedido(
            42L, clienteHuguinho, null,
            List.of(new ItemPedido(produtoCalabresa, 2)),
            Pedido.Status.NOVO, 0, 0, 0, 0, "Rua das Flores, 100"
        );
        when(pedidoService.submeter(eq("9001"), eq("Rua das Flores, 100"), anyList()))
            .thenReturn(pedidoCriado);

        // Act
        SubmeterPedidoResponse response = submeterPedidoUC.run(request);

        // Assert
        assertEquals(42L, response.getIdPedido());
        assertEquals("NOVO", response.getStatus());
        assertEquals("Rua das Flores, 100", response.getEnderecoEntrega());
        assertNotNull(response.getMensagem());
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de itens está vazia no request")
    void run_semItens_lancaExcecao() {
        SubmeterPedidoRequest request = new SubmeterPedidoRequest(
            "9001", "Rua A, 1", List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> submeterPedidoUC.run(request));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produtoId não existe no repositório")
    void run_produtoIdInexistente_lancaExcecao() {
        SubmeterPedidoRequest request = new SubmeterPedidoRequest(
            "9001", "Rua A, 1",
            List.of(new ItemPedidoRequest(999L, 1))
        );
        when(produtosRepository.recuperaProdutoPorid(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> submeterPedidoUC.run(request));
    }

    @Test
    @DisplayName("Deve lançar exceção quando request é nulo")
    void run_requestNulo_lancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> submeterPedidoUC.run(null));
    }
}
