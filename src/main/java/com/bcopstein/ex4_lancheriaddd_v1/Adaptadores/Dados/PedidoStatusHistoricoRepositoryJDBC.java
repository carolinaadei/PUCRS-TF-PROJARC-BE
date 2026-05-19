package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusHistoricoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

@Component
public class PedidoStatusHistoricoRepositoryJDBC implements PedidoStatusHistoricoRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoStatusHistoricoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void salvar(PedidoStatusHistorico historico) {
        String sql = "INSERT INTO pedido_status_historico (pedido_id, status, data_hora, responsavel) " +
                     "VALUES (?, ?, ?, ?)";
        this.jdbcTemplate.update(
            sql,
            historico.getPedidoId(),
            historico.getStatus().name(),
            historico.getDataHora(),
            historico.getResponsavel()
        );
    }
}