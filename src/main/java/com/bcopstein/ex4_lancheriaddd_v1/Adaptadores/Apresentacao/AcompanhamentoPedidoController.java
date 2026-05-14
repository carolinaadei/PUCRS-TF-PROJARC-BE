package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AcompanharPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AcompanhamentoPedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class AcompanhamentoPedidoController {

    private final AcompanharPedidoUC acompanharPedidoUC;

    public AcompanhamentoPedidoController(AcompanharPedidoUC acompanharPedidoUC) {
        this.acompanharPedidoUC = acompanharPedidoUC;
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public AcompanhamentoPedidoResponse acompanharPedido(
            @PathVariable(value = "id") long id,
            @RequestParam String cpf) {
        return acompanharPedidoUC.run(id, cpf);
    }
}