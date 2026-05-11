package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.StatusPedido;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    Pedido save(Pedido pedido);
    Optional<Pedido> findById(Long id);
    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);
    List<Pedido> findByStatusAndCriadoEmBetween(StatusPedido status, LocalDateTime inicio, LocalDateTime fim);

    /** UC8: pedidos entregues entre duas datas (todos os clientes). */
    List<Pedido> findEntreguesBetween(LocalDateTime inicio, LocalDateTime fim);

    /** UC9: pedidos entregues de um cliente entre duas datas. */
    List<Pedido> findEntreguesByClienteBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fim);

    /** UC4: pedidos do cliente nos últimos N dias (para cálculo de desconto). */
    int countPedidosClienteDesde(Long clienteId, LocalDateTime desde);
}
