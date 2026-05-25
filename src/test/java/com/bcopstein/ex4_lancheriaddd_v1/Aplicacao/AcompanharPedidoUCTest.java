package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AcompanhamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.AcompanhamentoPedidoService;

@ExtendWith(MockitoExtension.class)
class AcompanharPedidoUCTest {

    @Mock
    private AcompanhamentoPedidoService acompanhamentoService;

    @InjectMocks
    private AcompanharPedidoUC uc;

    private Cliente cliente;
    private Pedido pedido;
    private LocalDateTime pagamento;

    @BeforeEach
    void setUp() {
        pagamento = LocalDateTime.of(2026, 5, 14, 14, 30);
        cliente = new Cliente(null, "Huguinho Pato", "9001", "51985744566",
                "Rua das Flores, 100", "huguinho@email.com", null);
        pedido = new Pedido(1L, cliente, pagamento, List.of(),
                Pedido.Status.PREPARACAO, 5500, 550, 0, 6050);
    }

    @Test
    void run_deveRetornarResponseCompleto_quandoDadosValidos() {
        List<PedidoStatusHistorico> historico = List.of(
                new PedidoStatusHistorico(1L, 1L, Pedido.Status.NOVO,
                        LocalDateTime.of(2026, 5, 14, 14, 0), "cliente"),
                new PedidoStatusHistorico(2L, 1L, Pedido.Status.APROVADO,
                        LocalDateTime.of(2026, 5, 14, 14, 5), "sistema"),
                new PedidoStatusHistorico(3L, 1L, Pedido.Status.PAGO,
                        LocalDateTime.of(2026, 5, 14, 14, 30), "cliente"),
                new PedidoStatusHistorico(4L, 1L, Pedido.Status.PREPARACAO,
                        LocalDateTime.of(2026, 5, 14, 14, 45), "cozinha")
        );
        when(acompanhamentoService.consultarStatus(1L, "9001")).thenReturn(pedido);
        when(acompanhamentoService.consultarHistorico(1L)).thenReturn(historico);

        AcompanhamentoPedidoResponse response = uc.run(1L, "9001");

        assertThat(response.getIdPedido()).isEqualTo(1L);
        assertThat(response.getStatusAtual()).isEqualTo("PREPARACAO");
        assertThat(response.getDescricaoStatus()).isEqualTo("Pedido sendo preparado");
        assertThat(response.getEnderecoEntrega()).isEqualTo("Rua das Flores, 100");
        assertThat(response.getDataHoraPagamento()).isEqualTo(pagamento);
        assertThat(response.getHistorico()).hasSize(4);
        assertThat(response.getHistorico().get(0).status()).isEqualTo("NOVO");
        assertThat(response.getHistorico().get(0).responsavel()).isEqualTo("cliente");
        assertThat(response.getHistorico().get(3).status()).isEqualTo("PREPARACAO");
    }

    @Test
    void run_deveLancarIllegalArgument_quandoCpfNulo() {
        assertThatThrownBy(() -> uc.run(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF");
    }

    @Test
    void run_deveLancarIllegalArgument_quandoCpfVazio() {
        assertThatThrownBy(() -> uc.run(1L, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF");
    }

    @Test
    void run_deveRetornarHistoricoVazio_quandoPedidoSemTransicoes() {
        when(acompanhamentoService.consultarStatus(1L, "9001")).thenReturn(pedido);
        when(acompanhamentoService.consultarHistorico(1L)).thenReturn(List.of());

        AcompanhamentoPedidoResponse response = uc.run(1L, "9001");

        assertThat(response.getHistorico()).isEmpty();
    }
}
