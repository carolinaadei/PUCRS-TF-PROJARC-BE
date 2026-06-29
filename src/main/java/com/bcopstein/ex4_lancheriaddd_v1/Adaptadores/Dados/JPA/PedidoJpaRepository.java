package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.PedidoEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {

    @Query("SELECT DISTINCT p FROM PedidoEntity p " +
           "JOIN p.statusHistorico h " +
           "WHERE p.status = :status AND h.status = :status " +
           "AND h.dataHora BETWEEN :inicio AND :fim")
    List<PedidoEntity> findEntreguesByDataHora(
            @Param("status") Pedido.Status status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT DISTINCT p FROM PedidoEntity p " +
           "JOIN p.statusHistorico h " +
           "WHERE p.status = :status AND h.status = :status " +
           "AND p.cliente.cpf = :cpf " +
           "AND h.dataHora BETWEEN :inicio AND :fim")
    List<PedidoEntity> findEntreguesByClienteAndDataHora(
            @Param("status") Pedido.Status status,
            @Param("cpf") String cpf,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM PedidoEntity p " +
           "WHERE p.cliente.cpf = :cpf AND p.dataHoraPagamento >= :desde")
    long countPedidosRecentes(
            @Param("cpf") String cpf,
            @Param("desde") LocalDateTime desde);

    List<PedidoEntity> findByClienteCpf(String cpf);
}
