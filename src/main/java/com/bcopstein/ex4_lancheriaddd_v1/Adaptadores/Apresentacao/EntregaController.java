package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IEntregaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    private final IEntregaService entregaService;
    private final PedidoRepository pedidoRepository;

    public EntregaController(IEntregaService entregaService,
                              PedidoRepository pedidoRepository) {
        this.entregaService = entregaService;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping("/{pedidoId}/iniciar")
    public ResponseEntity<String> iniciarEntrega(@PathVariable long pedidoId) {
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));

        if (pedido.getStatus() != Pedido.Status.PRONTO) {
            return ResponseEntity.badRequest()
                    .body("Pedido deve estar PRONTO para iniciar entrega. Status atual: " + pedido.getStatus());
        }

        entregaService.iniciarEntrega(pedido);
        return ResponseEntity.ok("Entrega do pedido " + pedidoId + " iniciada. Acompanhe o status!");
    }

    @GetMapping("/{pedidoId}/status")
    public ResponseEntity<EntregaStatusResponse> status(@PathVariable long pedidoId) {
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedidoId));
        return ResponseEntity.ok(new EntregaStatusResponse(pedidoId, pedido.getStatus().name()));
    }

    public record EntregaStatusResponse(long pedidoId, String status) {}
}
