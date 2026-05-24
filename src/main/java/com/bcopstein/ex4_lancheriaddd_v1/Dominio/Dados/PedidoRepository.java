package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;
import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

/**
 * Porta de saída (repositório) para a entidade Pedido.
 * Seguindo o padrão DDD/Ports & Adapters do projeto, esta interface pertence
 * ao Domínio e é implementada na camada de Adaptadores/Dados.
 *
 * ATENÇÃO: este arquivo substitui/estende o PedidoRepository já existente.
 * Se o projeto já tiver PedidoRepository com buscarPorId/salvar, apenas
 * acrescente o método criar() à interface existente.
 */
public interface PedidoRepository {

    /** Persiste um novo pedido e retorna a instância com o ID gerado. */
    Pedido criar(Pedido pedido);

    /** Busca um pedido pelo seu identificador. */
    Pedido buscarPorId(long id);

    /** Persiste mudanças em um pedido existente (status, cancelamento, etc.). */
    void salvar(Pedido pedido);

    /** Busca pedidos com status ENTREGUE dentro do intervalo informado. */
    List<Pedido> buscarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim);
}
