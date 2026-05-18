package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    /** Persiste mudanças em um pedido existente (status, cancelamento, etc.). */
    Pedido salvar(Pedido pedido);

    Optional<Pedido> recuperaPorId(long id);

    List<Pedido> recuperaTodos();

    void atualizarStatus(long id, Pedido.Status status);

    /** Persiste um novo pedido e retorna a instância com o ID gerado. */
    Pedido criar(Pedido pedido);

    /** Busca um pedido pelo seu identificador. */
    Pedido buscarPorId(long id);

    List<Pedido> buscarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim);
}
