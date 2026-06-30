package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final IPaymentService paymentService;
    private final PedidoRepository pedidoRepository;

    public PagamentoController(IPaymentService paymentService,
                                PedidoRepository pedidoRepository) {
        this.paymentService = paymentService;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping("/{pedidoId}")
    public ResponseEntity<PagamentoResponse> processar(@PathVariable long pedidoId) {
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            return ResponseEntity.badRequest()
                    .body(new PagamentoResponse(pedidoId, false, "Pedido deve estar APROVADO. Status atual: " + pedido.getStatus()));
        }

        boolean aprovado = paymentService.processPayment(pedidoId, pedido.getValorCobrado());
        if (!aprovado) {
            return ResponseEntity.ok(new PagamentoResponse(pedidoId, false, "Pagamento recusado"));
        }
        return ResponseEntity.ok(new PagamentoResponse(pedidoId, true, "Pagamento aprovado"));
    }

    @GetMapping("/{pedidoId}/status")
    public ResponseEntity<PagamentoStatusResponse> status(@PathVariable long pedidoId) {
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));
        boolean pago = pedido.getStatus() == Pedido.Status.PAGO
                || pedido.getStatus() == Pedido.Status.AGUARDANDO
                || pedido.getStatus() == Pedido.Status.PREPARACAO
                || pedido.getStatus() == Pedido.Status.PRONTO
                || pedido.getStatus() == Pedido.Status.TRANSPORTE
                || pedido.getStatus() == Pedido.Status.ENTREGUE;
        return ResponseEntity.ok(new PagamentoStatusResponse(pedidoId, pedido.getStatus().name(), pago));
    }

    public record PagamentoResponse(long pedidoId, boolean aprovado, String mensagem) {}
    public record PagamentoStatusResponse(long pedidoId, String status, boolean pago) {}
}
