package com.bcopstein.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CalculadoraPreco;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CalculadoraPreco.ResultadoCalculo;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

/**
 * Testes unitários para {@link PedidoService}.
 *
 * Estratégia: Mockito para isolar o serviço de domínio dos adaptadores.
 * Nenhuma dependência de banco de dados é usada.
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

        @Mock
        private PedidoRepository pedidoRepository;

        @Mock
        private ClienteRepository clienteRepository;

        @Mock
        private CalculadoraPreco calculadoraPreco;

        @InjectMocks
        private PedidoService pedidoService;

        // ── Fixtures ─────────────────────────────────────────────────────────────

        private Cliente clienteValido;
        private Produto produtoValido;
        private List<ItemPedido> itensValidos;

        @BeforeEach
        void setUp() {
                clienteValido = new Cliente("9001", "Huguinho Pato", "51985744566",
                                "Rua das Flores, 100", "huguinho@email.com");

                Receita receita = new Receita(1L, "Pizza calabresa",
                                List.of(new Ingrediente(1L, "Disco de pizza")));
                produtoValido = new Produto(1L, "Pizza calabresa", receita, 5500);
                itensValidos = List.of(new ItemPedido(produtoValido, 2));
        }

        // ── Testes: submeter ──────────────────────────────────────────────────────

        @Test
        @DisplayName("Deve criar pedido com status NOVO quando dados são válidos")
        void submeter_dadosValidos_criaPedidoNovo() {
                // Arrange
                when(clienteRepository.recuperaPorCpf("9001")).thenReturn(Optional.of(clienteValido));
                when(calculadoraPreco.calcular(any())).thenReturn(new ResultadoCalculo(11000, 1100, 0, 12100));
                when(pedidoRepository.criar(any(Pedido.class))).thenAnswer(inv -> {
                        Pedido p = inv.getArgument(0);
                        p.setId(10L);
                        return p;
                });

                // Act
                Pedido resultado = pedidoService.submeter("9001", "Rua das Flores, 100", itensValidos);

                // Assert
                assertNotNull(resultado);
                assertEquals(10L, resultado.getId());
                assertEquals(Pedido.Status.NOVO, resultado.getStatus());
                assertEquals("Rua das Flores, 100", resultado.getEnderecoEntrega());
                assertEquals("9001", resultado.getCliente().getCpf());
                verify(pedidoRepository).criar(any(Pedido.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF do cliente é nulo")
        void submeter_cpfNulo_lancaExcecao() {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> pedidoService.submeter(null, "Rua A, 1", itensValidos));
                assertTrue(ex.getMessage().contains("CPF"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando CPF do cliente é vazio")
        void submeter_cpfVazio_lancaExcecao() {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> pedidoService.submeter("   ", "Rua A, 1", itensValidos));
                assertTrue(ex.getMessage().contains("CPF"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando endereço de entrega é nulo")
        void submeter_enderecoNulo_lancaExcecao() {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> pedidoService.submeter("9001", null, itensValidos));
                assertTrue(ex.getMessage().contains("ndere"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista de itens é vazia")
        void submeter_semItens_lancaExcecao() {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> pedidoService.submeter("9001", "Rua A, 1", List.of()));
                assertTrue(ex.getMessage().contains("item"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando lista de itens é nula")
        void submeter_itensNulos_lancaExcecao() {
                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> pedidoService.submeter("9001", "Rua A, 1", null));
                assertTrue(ex.getMessage().contains("item"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existe no cadastro")
        void submeter_clienteNaoEncontrado_lancaExcecao() {
                when(clienteRepository.recuperaPorCpf("9999")).thenReturn(Optional.empty());

                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> pedidoService.submeter("9999", "Rua A, 1", itensValidos));
                assertTrue(ex.getMessage().contains("9999"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando item tem quantidade zero")
        void submeter_itemComQuantidadeZero_lancaExcecao() {
                List<ItemPedido> itensInvalidos = List.of(new ItemPedido(produtoValido, 0));

                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                                () -> pedidoService.submeter("9001", "Rua A, 1", itensInvalidos));
                assertTrue(ex.getMessage().contains("Quantidade"));
        }

        // ── Testes: cancelar ─────────────────────────────────────────────────────

        @Test
        @DisplayName("Deve cancelar pedido com status NOVO")
        void cancelar_pedidoNovo_cancelaComSucesso() {
                Pedido pedidoNovo = new Pedido(1L, clienteValido, null, itensValidos,
                                Pedido.Status.NOVO, 0, 0, 0, 0, "Rua A, 1");
                when(pedidoRepository.recuperaPorId(1L)).thenReturn(Optional.of(pedidoNovo));

                Pedido resultado = pedidoService.cancelar(1L, "cliente");

                assertEquals(Pedido.Status.CANCELADO, resultado.getStatus());
                assertEquals("cliente", resultado.getCanceladoPor());
                assertNotNull(resultado.getDataHoraCancelamento());
                verify(pedidoRepository).salvar(pedidoNovo);
        }

        @Test
        @DisplayName("Deve cancelar pedido com status APROVADO")
        void cancelar_pedidoAprovado_cancelaComSucesso() {
                Pedido pedidoAprovado = new Pedido(2L, clienteValido, null, itensValidos,
                                Pedido.Status.APROVADO, 5500, 550, 0, 6050, "Rua A, 1");
                when(pedidoRepository.recuperaPorId(2L)).thenReturn(Optional.of(pedidoAprovado));

                Pedido resultado = pedidoService.cancelar(2L, "admin");

                assertEquals(Pedido.Status.CANCELADO, resultado.getStatus());
        }

        @Test
        @DisplayName("Não deve cancelar pedido já PAGO")
        void cancelar_pedidoPago_lancaExcecao() {
                Pedido pedidoPago = new Pedido(3L, clienteValido, null, itensValidos,
                                Pedido.Status.PAGO, 5500, 550, 0, 6050, "Rua A, 1");
                when(pedidoRepository.recuperaPorId(3L)).thenReturn(Optional.of(pedidoPago));

                RuntimeException ex = assertThrows(RuntimeException.class,
                                () -> pedidoService.cancelar(3L, "cliente"));
                assertTrue(ex.getMessage().contains("PAGO"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando pedido não existe")
        void cancelar_pedidoInexistente_lancaExcecao() {
                when(pedidoRepository.recuperaPorId(999L)).thenReturn(Optional.empty());

                RuntimeException ex = assertThrows(RuntimeException.class,
                                () -> pedidoService.cancelar(999L, "cliente"));
                assertTrue(ex.getMessage().contains("não encontrado"));
        }
}