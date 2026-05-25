package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class ListarPedidosEntreguesUC {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Lista todos os pedidos entregues entre duas datas.
     */
    public List<PedidoResponse> run(LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> pedidos = pedidoService.listarEntreguesEntre(inicio, fim);
        return pedidos.stream()
            .map(p -> new PedidoResponse(
                p.getId(),
                p.getCliente() != null ? p.getCliente().getCpf() : "N/A",
                p.getStatus().name(),
                p.getValorCobrado(),
                p.getDataHoraPagamento()
            ))
            .toList();
    }
}
