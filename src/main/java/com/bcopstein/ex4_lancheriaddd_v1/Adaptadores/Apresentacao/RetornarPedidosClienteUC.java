package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RetornarPedidosClienteUC {

    private final PedidoService pedidoService;

    public RetornarPedidosClienteUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public List<PedidoResponse> run(String clienteCpf) {
        return pedidoService.listarPorCliente(clienteCpf)
                .stream()
                .map(PedidoResponse::from)
                .toList();
    }
}