package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.util.List;

/**
 * Payload recebido pelo cliente para submissão de um novo pedido.
 * Contém o CPF do cliente, endereço de entrega e a lista de itens desejados.
 */
public class SubmeterPedidoRequest {

    private String clienteCpf;
    private String enderecoEntrega;
    private List<ItemPedidoRequest> itens;

    public SubmeterPedidoRequest() {}

    public SubmeterPedidoRequest(String clienteCpf, String enderecoEntrega, List<ItemPedidoRequest> itens) {
        this.clienteCpf = clienteCpf;
        this.enderecoEntrega = enderecoEntrega;
        this.itens = itens;
    }

    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }

    public String getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(String enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public List<ItemPedidoRequest> getItens() { return itens; }
    public void setItens(List<ItemPedidoRequest> itens) { this.itens = itens; }

    // ──────────────────────────────────────────────
    // Inner record: representa cada linha do pedido
    // ──────────────────────────────────────────────
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
