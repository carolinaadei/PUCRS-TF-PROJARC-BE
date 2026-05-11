package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.UseCases;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;

/**
 * UC4 - Submeter pedido para aprovação (requer autenticação).
 * Retorna o pedido aprovado com preço calculado,
 * ou pedido negado com os itens sem estoque identificados.
 */
public interface SubmeterPedidoUC {
    PedidoResponse executar(Long clienteId, SubmeterPedidoRequest request);
}
