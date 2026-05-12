package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private CancelarPedidoUC cancelarPedidoUC;

    public PedidoController(CancelarPedidoUC cancelarPedidoUC) {
        this.cancelarPedidoUC = cancelarPedidoUC;
    }

    @PostMapping("/{id}/cancelar")
    @CrossOrigin("*")
    public CancelarPedidoResponse cancelarPedido(
            @PathVariable(value = "id") long id,
            @RequestParam String canceladoPor) {
        return cancelarPedidoUC.run(id, canceladoPor);
    }
}