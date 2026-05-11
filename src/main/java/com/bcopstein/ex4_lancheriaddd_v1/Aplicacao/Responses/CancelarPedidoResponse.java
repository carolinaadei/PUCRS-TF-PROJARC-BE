package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;

public class CancelarPedidoResponse {
    private long idPedido;
    private String status;
    private String mensagem;
    private String canceladoPor;
    private LocalDateTime dataHoraCancelamento;

    public CancelarPedidoResponse(long idPedido, String status, String mensagem, 
                                   String canceladoPor, LocalDateTime dataHoraCancelamento) {
        this.idPedido = idPedido;
        this.status = status;
        this.mensagem = mensagem;
        this.canceladoPor = canceladoPor;
        this.dataHoraCancelamento = dataHoraCancelamento;
    }

    public long getIdPedido() { return idPedido; }
    public String getStatus() { return status; }
    public String getMensagem() { return mensagem; }
    public String getCanceladoPor() { return canceladoPor; }
    public LocalDateTime getDataHoraCancelamento() { return dataHoraCancelamento; }
}