package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/interno")
public class InternalController {

    private static final Logger log = LoggerFactory.getLogger(InternalController.class);

    @Value("${internal.secret:pizzaria-delivery-secret}")
    private String internalSecret;

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;

    public InternalController(PedidoRepository pedidoRepository,
                               PedidoStatusRepository statusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
    }

    @PostMapping("/pedidos/{id}/transporte")
    public ResponseEntity<Void> marcarTransporte(
            @PathVariable long id,
            @RequestHeader(value = "X-Internal-Secret", required = false) String secret) {
        if (!internalSecret.equals(secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pedido pedido = pedidoRepository.recuperaPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));

        if (pedido.getStatus() == Pedido.Status.TRANSPORTE) {
            return ResponseEntity.ok().build();
        }

        pedido.setStatus(Pedido.Status.TRANSPORTE);
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.TRANSPORTE, LocalDateTime.now(), "entregador"));

        log.info("Pedido {} marcado como TRANSPORTE pelo serviço de entregas.", id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pedidos/{id}/entregue")
    public ResponseEntity<Void> marcarEntregue(
            @PathVariable long id,
            @RequestHeader(value = "X-Internal-Secret", required = false) String secret) {
        if (!internalSecret.equals(secret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pedido pedido = pedidoRepository.recuperaPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));

        if (pedido.getStatus() == Pedido.Status.ENTREGUE) {
            return ResponseEntity.ok().build();
        }

        pedido.setStatus(Pedido.Status.ENTREGUE);
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
            pedido.getId(), Pedido.Status.ENTREGUE, LocalDateTime.now(), "entregador"));

        log.info("Pedido {} marcado como ENTREGUE pelo serviço de entregas.", id);
        return ResponseEntity.ok().build();
    }
}
