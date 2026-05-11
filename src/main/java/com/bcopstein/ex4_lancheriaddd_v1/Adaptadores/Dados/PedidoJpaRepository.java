package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoJpaRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);

    @Query("SELECT p FROM Pedido p WHERE p.status = 'ENTREGUE' " +
           "AND p.criadoEm BETWEEN :inicio AND :fim")
    List<Pedido> findEntreguesBetween(@Param("inicio") LocalDateTime inicio,
                                      @Param("fim") LocalDateTime fim);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId " +
           "AND p.status = 'ENTREGUE' AND p.criadoEm BETWEEN :inicio AND :fim")
    List<Pedido> findEntreguesByClienteBetween(@Param("clienteId") Long clienteId,
                                               @Param("inicio") LocalDateTime inicio,
                                               @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId " +
           "AND p.criadoEm >= :desde AND p.status NOT IN ('CANCELADO', 'NOVO')")
    int countPedidosClienteDesde(@Param("clienteId") Long clienteId,
                                 @Param("desde") LocalDateTime desde);
}
