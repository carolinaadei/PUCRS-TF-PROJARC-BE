package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CozinhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cozinha")
public class CozinhaController {

    private final CozinhaService cozinhaService;
    private final PedidoRepository pedidoRepository;

    public CozinhaController(CozinhaService cozinhaService,
                              PedidoRepository pedidoRepository) {
        this.cozinhaService = cozinhaService;
        this.pedidoRepository = pedidoRepository;
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<String> enviarParaCozinha(@PathVariable long id) {
        Pedido pedido = pedidoRepository.recuperaPorId(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != Pedido.Status.PAGO) {
            return ResponseEntity.badRequest()
                .body("Pedido deve estar PAGO. Status atual: " + pedido.getStatus());
        }

        cozinhaService.chegadaDePedido(pedido);
        return ResponseEntity.ok("Pedido " + id + " enviado para a cozinha. Acompanhe o status!");
    }
}