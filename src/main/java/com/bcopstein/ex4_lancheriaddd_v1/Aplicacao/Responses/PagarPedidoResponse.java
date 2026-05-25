package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;

public class PagarPedidoResponse {
    private long idPedido;
    private String status;
    private String mensagem;
    private LocalDateTime dataHoraPagamento;

    public PagarPedidoResponse(long idPedido, String status, String mensagem, LocalDateTime dataHoraPagamento) {
        this.idPedido = idPedido;
        this.status = status;
        this.mensagem = mensagem;
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public long getIdPedido() { return idPedido; }
    public String getStatus() { return status; }
    public String getMensagem() { return mensagem; }
    public LocalDateTime getDataHoraPagamento() { return dataHoraPagamento; }
}