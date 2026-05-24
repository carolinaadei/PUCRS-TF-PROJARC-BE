package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

/**
 * Porta de saída: acesso ao histórico de mudanças de status de um pedido.
 * Pertence ao Domínio — implementada na camada Adaptadores/Dados.
 */
public interface PedidoStatusRepository {

    /** Retorna o histórico de status de um pedido ordenado cronologicamente. */
    List<PedidoStatusHistorico> buscarHistoricoPorPedidoId(long pedidoId);

    /** Registra uma nova entrada no histórico de status. */
    void registrar(PedidoStatusHistorico historico);
}
