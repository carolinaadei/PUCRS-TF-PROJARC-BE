package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class CancelarPedidoUC {

    @Autowired
    private PedidoService pedidoService;

    public CancelarPedidoResponse run(long idPedido, String canceladoPor) {
        Pedido pedido = pedidoService.cancelar(idPedido, canceladoPor);

        return new CancelarPedidoResponse(
            pedido.getId(),
            pedido.getStatus().name(),
            "Pedido cancelado com sucesso",
            pedido.getCanceladoPor(),
            pedido.getDataHoraCancelamento()
        );
    }
}