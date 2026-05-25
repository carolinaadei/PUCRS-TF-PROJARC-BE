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

    /**
     * Processa o pagamento de um pedido aprovado e o envia para a cozinha.
     */
    public PagarPedidoResponse run(long idPedido) {
        Pedido pedido = pedidoService.pagar(idPedido);

        return new PagarPedidoResponse(
            pedido.getId(),
            pedido.getStatus().name(),
            "Pagamento aprovado! Pedido enviado para a cozinha.",
            pedido.getDataHoraPagamento()
        );
    }
}