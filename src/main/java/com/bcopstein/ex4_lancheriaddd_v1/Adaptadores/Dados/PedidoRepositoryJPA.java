package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Repository
public interface PedidoRepositoryJPA extends JpaRepository<Pedido, Long>, PedidoRepository {

    @Override
    default Pedido criar(Pedido pedido) {
        return save(pedido);
    }

    @Override
    default Pedido buscarPorId(long id) {
        return findById(id).orElse(null);
    }

    @Override
    default void salvar(Pedido pedido) {
        save(pedido);
    }

    @Query(value = """
            SELECT p.* FROM pedidos p
            JOIN pedido_status_historico h ON h.pedido_id = p.id
            WHERE p.status = 'ENTREGUE'
              AND h.status = 'ENTREGUE'
              AND h.data_hora BETWEEN :inicio AND :fim
            """, nativeQuery = true)
    @Override
    List<Pedido> buscarEntreguesEntre(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    @Query(value = """
            SELECT p.* FROM pedidos p
            JOIN pedido_status_historico h ON h.pedido_id = p.id
            WHERE p.status = 'ENTREGUE'
              AND h.status = 'ENTREGUE'
              AND p.cliente_cpf = :cpf
              AND h.data_hora BETWEEN :inicio AND :fim
            """, nativeQuery = true)
    @Override
    List<Pedido> buscarEntreguesPorClienteEntre(
            @Param("cpf") String clienteCpf,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}
