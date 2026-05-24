package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.time.LocalDateTime;

/**
 * Entidade de domínio que representa uma entrada no histórico de status de um pedido.
 *
 * Cada vez que um pedido muda de status, uma entrada é registrada com:
 *  - o status que foi atingido
 *  - a data/hora exata da transição
 *  - quem (ou qual sistema) provocou a mudança
 *
 * Isso permite ao cliente acompanhar toda a evolução do pedido com rastreabilidade.
 */
public class PedidoStatusHistorico {

    private long id;
    private long pedidoId;
    private Pedido.Status status;
    private LocalDateTime dataHora;
    private String responsavel; // ex: "cliente", "cozinha", "sistema"

    public PedidoStatusHistorico(long id, long pedidoId, Pedido.Status status,
                                  LocalDateTime dataHora, String responsavel) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.status = status;
        this.dataHora = dataHora;
        this.responsavel = responsavel;
    }

    /** Construtor para criação (sem ID — gerado pelo banco). */
    public PedidoStatusHistorico(long pedidoId, Pedido.Status status,
                                  LocalDateTime dataHora, String responsavel) {
        this(0L, pedidoId, status, dataHora, responsavel);
    }

    public long getId() { return id; }
    public long getPedidoId() { return pedidoId; }
    public Pedido.Status getStatus() { return status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getResponsavel() { return responsavel; }
}