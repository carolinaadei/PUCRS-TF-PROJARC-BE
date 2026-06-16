package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.util.List;

/**
 * Payload recebido pelo cliente para submissão de um novo pedido.
 * Contém o CPF do cliente, endereço de entrega e a lista de itens desejados.
 */
public class RetornarPedidosRequest {

    private String clienteCpf;

    public RetornarPedidosRequest() {}

    public RetornarPedidosRequest(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }

    public static class ItemPedidoRequest {
        private Long produtoId;
        private int quantidade;

        public ItemPedidoRequest() {}

        public ItemPedidoRequest(Long produtoId, int quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
        }

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    }
}
