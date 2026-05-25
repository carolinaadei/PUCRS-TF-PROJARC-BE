package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

@ExtendWith(MockitoExtension.class)
class AcompanhamentoPedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoStatusRepository pedidoStatusRepository;

    @InjectMocks
    private AcompanhamentoPedidoService service;

    private Cliente clienteHuguinho;
    private Pedido pedidoAprovado;

    @BeforeEach
    void setUp() {
        clienteHuguinho = new Cliente(null, "Huguinho Pato", "9001", "51985744566",
                "Rua das Flores, 100", "huguinho@email.com", null);
        pedidoAprovado = new Pedido(1L, clienteHuguinho, null, List.of(),
                Pedido.Status.APROVADO, 5500, 550, 0, 6050);
    }

    @Test
    void consultarStatus_deveRetornarPedido_quandoCpfCorreto() {
        when(pedidoRepository.recuperaPorId(1L)).thenReturn(Optional.of(pedidoAprovado));

        Pedido resultado = service.consultarStatus(1L, "9001");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getStatus()).isEqualTo(Pedido.Status.APROVADO);
    }

    @Test
    void consultarStatus_deveLancarIllegalArgument_quandoPedidoNaoExiste() {
        when(pedidoRepository.recuperaPorId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.consultarStatus(99L, "9001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    @Test
    void consultarStatus_deveLancarAcessoNaoAutorizado_quandoCpfDiferente() {
        when(pedidoRepository.recuperaPorId(1L)).thenReturn(Optional.of(pedidoAprovado));

        assertThatThrownBy(() -> service.consultarStatus(1L, "9999"))
                .isInstanceOf(AcessoNaoAutorizadoException.class)
                .hasMessageContaining("1");
    }

    @Test
    void consultarStatus_deveLancarAcessoNaoAutorizado_quandoClienteNull() {
        Pedido semCliente = new Pedido(1L, null, null, List.of(),
                Pedido.Status.NOVO, 5500, 550, 0, 6050);
        when(pedidoRepository.recuperaPorId(1L)).thenReturn(Optional.of(semCliente));

        assertThatThrownBy(() -> service.consultarStatus(1L, "9001"))
                .isInstanceOf(AcessoNaoAutorizadoException.class);
    }

    @Test
    void consultarHistorico_deveRetornarListaOrdenada() {
        LocalDateTime t1 = LocalDateTime.of(2026, 5, 14, 10, 0);
        LocalDateTime t2 = LocalDateTime.of(2026, 5, 14, 10, 5);
        List<PedidoStatusHistorico> historico = List.of(
                new PedidoStatusHistorico(1L, 1L, Pedido.Status.NOVO, t1, "cliente"),
                new PedidoStatusHistorico(2L, 1L, Pedido.Status.APROVADO, t2, "sistema")
        );
        when(pedidoStatusRepository.buscarHistoricoPorPedidoId(1L)).thenReturn(historico);

        List<PedidoStatusHistorico> resultado = service.consultarHistorico(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getStatus()).isEqualTo(Pedido.Status.NOVO);
        assertThat(resultado.get(1).getStatus()).isEqualTo(Pedido.Status.APROVADO);
    }
}
