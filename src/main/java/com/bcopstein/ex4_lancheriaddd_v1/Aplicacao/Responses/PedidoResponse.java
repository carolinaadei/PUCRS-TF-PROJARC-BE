package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

import java.time.LocalDateTime;

public class PedidoResponse {
    private long id;
    private String clienteCpf;
    private String status;
    private double valorCobrado;
    private LocalDateTime dataHoraPagamento;

    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente() != null ? pedido.getCliente().getCpf() : null,
                pedido.getStatus().name(),
                pedido.getValorCobrado(),
                pedido.getDataHoraPagamento());
    }

    public PedidoResponse(long id, String clienteCpf, String status,
                          double valorCobrado, LocalDateTime dataHoraPagamento) {
        this.id = id;
        this.clienteCpf = clienteCpf;
        this.status = status;
        this.valorCobrado = valorCobrado;
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public long getId() { return id; }
    public String getClienteCpf() { return clienteCpf; }
    public String getStatus() { return status; }
    public double getValorCobrado() { return valorCobrado; }
    public LocalDateTime getDataHoraPagamento() { return dataHoraPagamento; }
}