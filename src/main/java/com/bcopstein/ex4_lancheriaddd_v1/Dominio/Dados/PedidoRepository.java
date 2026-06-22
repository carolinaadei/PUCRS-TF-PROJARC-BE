package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository {

    /** Persiste um novo pedido e retorna a instância com o ID gerado. */
    Pedido criar(Pedido pedido);

    /** Persiste mudanças em um pedido existente (status, cancelamento, pagamento, etc.). */
    void salvar(Pedido pedido);

    Optional<Pedido> recuperaPorId(long id);

    List<Pedido> recuperaTodos();

    void atualizarStatus(long id, Pedido.Status status);

    List<Pedido> buscarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim);

    List<Pedido> buscarEntreguesPorClienteEntre(String clienteCpf, LocalDateTime inicio, LocalDateTime fim);

    long contarPedidosRecentes(String clienteCpf, LocalDateTime desde);

    List<Pedido> buscarPorCliente(String clienteCpf);
}
