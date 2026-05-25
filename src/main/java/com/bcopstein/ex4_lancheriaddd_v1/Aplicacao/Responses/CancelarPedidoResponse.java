package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public class CancelarPedidoResponse {

    private long idPedido;
    private String status;
    private String mensagem;

    public CancelarPedidoResponse(long idPedido, String status, String mensagem) {
        this.idPedido = idPedido;
        this.status = status;
        this.mensagem = mensagem;
    }

    public long getIdPedido() { return idPedido; }
    public String getStatus() { return status; }
    public String getMensagem() { return mensagem; }
}
