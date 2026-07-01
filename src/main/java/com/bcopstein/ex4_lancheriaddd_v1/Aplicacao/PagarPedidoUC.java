package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class PagarPedidoUC {

    @Autowired
    private PedidoService pedidoService;

    public PagarPedidoResponse run(long idPedido) {
        Pedido pedido = pedidoService.pagar(idPedido);

        return new PagarPedidoResponse(
            pedido.getId(),
            pedido.getStatus().name(),
            "Pagamento aprovado! Envie o pedido para a cozinha em POST /cozinha/{id}/enviar.",
            pedido.getDataHoraPagamento()
        );
    }
}