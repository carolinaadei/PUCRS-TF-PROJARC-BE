package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class CancelarPedidoUC {

    private final PedidoService pedidoService;

    @Autowired
    public CancelarPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public CancelarPedidoResponse run(long id, String canceladoPor) {
        Pedido pedido = pedidoService.cancelar(id, canceladoPor);
        return new CancelarPedidoResponse(
            pedido.getId(),
            pedido.getStatus().name(),
            "Pedido cancelado com sucesso"
        );
    }
}
