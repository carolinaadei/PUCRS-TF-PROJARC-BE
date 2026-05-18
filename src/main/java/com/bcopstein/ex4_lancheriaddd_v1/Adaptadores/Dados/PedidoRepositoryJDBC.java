package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Component
public class PedidoRepositoryJDBC implements PedidoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido criar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (cliente_cpf, status, valor, impostos, desconto, " +
                "valor_cobrado, endereco_entrega) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sqlPedido, new String[] { "id" });
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setString(2, pedido.getStatus().name());
            ps.setDouble(3, pedido.getValor());
            ps.setDouble(4, pedido.getImpostos());
            ps.setDouble(5, pedido.getDesconto());
            ps.setDouble(6, pedido.getValorCobrado());
            ps.setString(7, pedido.getEnderecoEntrega());
            return ps;
        }, keyHolder);

        long idGerado = keyHolder.getKey().longValue();
        pedido.setId(idGerado);

        String sqlItem = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";

        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(
                    sqlItem,
                    idGerado,
                    item.getItem().getId(),
                    item.getQuantidade());
        }

        return pedido;
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
                            new Cliente(rs.getString("cliente_cpf"), null, null, null, null),
                            rs.getObject("data_hora_pagamento", LocalDateTime.class),
                            null,
                            Pedido.Status.valueOf(rs.getString("status")),
                            rs.getDouble("valor"),
                            rs.getDouble("impostos"),
                            rs.getDouble("desconto"),
                            rs.getDouble("valor_cobrado"));
                    p.setCanceladoPor(rs.getString("cancelado_por"));
                    p.setDataHoraCancelamento(rs.getObject("data_hora_cancelamento", LocalDateTime.class));
                    return p;
                });
        if (pedidos.isEmpty()) {
            return null;
        }
        return pedidos.getFirst();
    }

    @Override
    public void salvar(Pedido pedido) {
        String sql = "UPDATE pedidos SET status = ?, cancelado_por = ?, data_hora_cancelamento = ?, " +
                "data_hora_pagamento = ? WHERE id = ?";
        this.jdbcTemplate.update(
                sql,
                pedido.getStatus().name(),
                pedido.getCanceladoPor(),
                pedido.getDataHoraCancelamento(),
                pedido.getDataHoraPagamento(),
                pedido.getId());
    }

    @Override
    public List<Pedido> buscarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT p.id, p.cliente_cpf, p.status, p.valor, p.impostos, p.desconto, p.valor_cobrado, " +
                "p.data_hora_pagamento, p.cancelado_por, p.data_hora_cancelamento " +
                "FROM pedidos p " +
                "JOIN pedido_status_historico h ON h.pedido_id = p.id " +
                "WHERE p.status = 'ENTREGUE' AND h.status = 'ENTREGUE' " +
                "AND h.data_hora BETWEEN ? AND ?";
        return this.jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setObject(1, inicio);
                    ps.setObject(2, fim);
                },
                (rs, rowNum) -> {
                    Pedido p = new Pedido(
                            rs.getLong("id"),
                            new Cliente(rs.getString("cliente_cpf"), null, null, null, null),
                            rs.getObject("data_hora_pagamento", LocalDateTime.class),
                            null,
                            Pedido.Status.valueOf(rs.getString("status")),
                            rs.getDouble("valor"),
                            rs.getDouble("impostos"),
                            rs.getDouble("desconto"),
                            rs.getDouble("valor_cobrado"));
                    p.setCanceladoPor(rs.getString("cancelado_por"));
                    p.setDataHoraCancelamento(rs.getObject("data_hora_cancelamento", LocalDateTime.class));
                    return p;
                });
    }

    @Override
    public List<Pedido> buscarEntreguesPorClienteEntre(String clienteCpf, LocalDateTime inicio, LocalDateTime fim) {
        String sql = "SELECT p.id, p.cliente_cpf, p.status, p.valor, p.impostos, p.desconto, p.valor_cobrado, " +
                "p.data_hora_pagamento, p.cancelado_por, p.data_hora_cancelamento " +
                "FROM pedidos p " +
                "JOIN pedido_status_historico h ON h.pedido_id = p.id " +
                "WHERE p.status = 'ENTREGUE' AND h.status = 'ENTREGUE' AND p.cliente_cpf = ? AND h.data_hora BETWEEN ? AND ?";
        return this.jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setString(1, clienteCpf);
                    ps.setObject(2, inicio);
                    ps.setObject(3, fim);
                },
                (rs, rowNum) -> {
                    Pedido p = new Pedido(
                            rs.getLong("id"),
                            new Cliente(rs.getString("cliente_cpf"), null, null, null, null),
                            rs.getObject("data_hora_pagamento", LocalDateTime.class),
                            null,
                            Pedido.Status.valueOf(rs.getString("status")),
                            rs.getDouble("valor"),
                            rs.getDouble("impostos"),
                            rs.getDouble("desconto"),
                            rs.getDouble("valor_cobrado"));
                    p.setCanceladoPor(rs.getString("cancelado_por"));
                    p.setDataHoraCancelamento(rs.getObject("data_hora_cancelamento", LocalDateTime.class));
                    return p;
                });
    }
}
