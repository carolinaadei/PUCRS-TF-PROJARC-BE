package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

/**
 * Resposta retornada após a submissão bem-sucedida de um pedido.
 * Confirma o ID do pedido criado e seu status inicial (NOVO).
 */
public class SubmeterPedidoResponse {

    private long idPedido;
    private String status;
    private String mensagem;
    private String enderecoEntrega;

    public SubmeterPedidoResponse(long idPedido, String status, String mensagem, String enderecoEntrega) {
        this.idPedido = idPedido;
        this.status = status;
        this.mensagem = mensagem;
        this.enderecoEntrega = enderecoEntrega;
    }

    public long getIdPedido() { return idPedido; }
    public String getStatus() { return status; }
    public String getMensagem() { return mensagem; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
}
