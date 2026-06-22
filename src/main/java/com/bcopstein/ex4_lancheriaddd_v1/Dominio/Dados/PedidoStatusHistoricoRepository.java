package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

public interface PedidoStatusHistoricoRepository {
    /**
     * Registra uma nova entrada no histórico de status do pedido.
     */
    void salvar(PedidoStatusHistorico historico);
}