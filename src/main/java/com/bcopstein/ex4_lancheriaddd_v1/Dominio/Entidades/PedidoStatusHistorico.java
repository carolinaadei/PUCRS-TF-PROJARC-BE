package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.time.LocalDateTime;

public class PedidoStatusHistorico {
    private long id;
    private long pedidoId;
    private Pedido.Status status;
    private LocalDateTime dataHora;
    private String responsavel;

    public PedidoStatusHistorico(long pedidoId, Pedido.Status status,
                                  LocalDateTime dataHora, String responsavel) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.dataHora = dataHora;
        this.responsavel = responsavel;
    }

    public long getId() { return id; }
    public long getPedidoId() { return pedidoId; }
    public Pedido.Status getStatus() { return status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getResponsavel() { return responsavel; }
}