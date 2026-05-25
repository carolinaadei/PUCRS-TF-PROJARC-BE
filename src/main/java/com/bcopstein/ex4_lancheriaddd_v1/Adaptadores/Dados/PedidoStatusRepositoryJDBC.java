package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

/**
 * Adaptador de dados: implementação JDBC da porta PedidoStatusRepository.
 * Segue o mesmo padrão dos demais repositórios JDBC do projeto.
 */
@Repository
public class PedidoStatusRepositoryJDBC implements PedidoStatusRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoStatusRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PedidoStatusHistorico> buscarHistoricoPorPedidoId(long pedidoId) {
        String sql =
            "SELECT id, pedido_id, status, data_hora, responsavel " +
            "FROM pedido_status_historico " +
            "WHERE pedido_id = ? " +
            "ORDER BY data_hora ASC";

        return jdbcTemplate.query(
            sql,
            ps -> ps.setLong(1, pedidoId),
            (rs, rowNum) -> new PedidoStatusHistorico(
                rs.getLong("id"),
                rs.getLong("pedido_id"),
                Pedido.Status.valueOf(rs.getString("status")),
                rs.getObject("data_hora", LocalDateTime.class),
                rs.getString("responsavel")
            )
        );
    }

    @Override
    public void registrar(PedidoStatusHistorico historico) {
        String sql =
            "INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) " +
            "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, historico.getPedidoId());
            ps.setString(2, historico.getStatus().name());
            ps.setObject(3, historico.getDataHora());
            ps.setString(4, historico.getResponsavel());
            return ps;
        }, keyHolder);
    }
}