package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class PedidoRepositoryJDBC implements PedidoRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido buscarPorId(long id) {
        String sql = "SELECT id, cliente_cpf, status, valor, impostos, desconto, valor_cobrado, " +
                     "data_hora_pagamento, cancelado_por, data_hora_cancelamento FROM pedidos WHERE id = ?";
        List<Pedido> pedidos = this.jdbcTemplate.query(
            sql,
            ps -> ps.setLong(1, id),
            (rs, rowNum) -> {
                Pedido p = new Pedido(
                    rs.getLong("id"),
                    null,
                    rs.getObject("data_hora_pagamento", LocalDateTime.class),
                    null,
                    Pedido.Status.valueOf(rs.getString("status")),
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado")
                );
                p.setCanceladoPor(rs.getString("cancelado_por"));
                p.setDataHoraCancelamento(rs.getObject("data_hora_cancelamento", LocalDateTime.class));
                return p;
            }
        );
        if (pedidos.isEmpty()) {
            return null;
        }
        return pedidos.getFirst();
    }

    @Override
    public void salvar(Pedido pedido) {
        String sql = "UPDATE pedidos SET status = ?, cancelado_por = ?, data_hora_cancelamento = ? WHERE id = ?";
        this.jdbcTemplate.update(
            sql,
            pedido.getStatus().name(),
            pedido.getCanceladoPor(),
            pedido.getDataHoraCancelamento(),
            pedido.getId()
        );
    }
}