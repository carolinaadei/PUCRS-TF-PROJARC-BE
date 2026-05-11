package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PedidoRepositoryImpl implements PedidoRepository {

    private final PedidoJpaRepository jpa;

    @Override public Pedido save(Pedido p)                   { return jpa.save(p); }
    @Override public Optional<Pedido> findById(Long id)      { return jpa.findById(id); }

    @Override
    public List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status) {
        return jpa.findByClienteIdAndStatus(clienteId, status);
    }

    @Override
    public List<Pedido> findByStatusAndCriadoEmBetween(StatusPedido status, LocalDateTime i, LocalDateTime f) {
        return jpa.findEntreguesBetween(i, f);
    }

    @Override
    public List<Pedido> findEntreguesBetween(LocalDateTime inicio, LocalDateTime fim) {
        return jpa.findEntreguesBetween(inicio, fim);
    }

    @Override
    public List<Pedido> findEntreguesByClienteBetween(Long clienteId, LocalDateTime inicio, LocalDateTime fim) {
        return jpa.findEntreguesByClienteBetween(clienteId, inicio, fim);
    }

    @Override
    public int countPedidosClienteDesde(Long clienteId, LocalDateTime desde) {
        return jpa.countPedidosClienteDesde(clienteId, desde);
    }
}
