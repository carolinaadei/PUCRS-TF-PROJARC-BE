package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class ConfirmarEntregaUC {

    @Autowired
    private PedidoService pedidoService;

    public void run(long pedidoId) {
        pedidoService.confirmarEntrega(pedidoId);
    }
}
