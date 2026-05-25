package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class ItemPedido {

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto item;

    @Column(name = "quantidade")
    private int quantidade;

    // Construtor vazio obrigatório para JPA
    protected ItemPedido() {}

    public ItemPedido(Produto item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    public Produto getItem() { return item; }
    public int getQuantidade() { return quantidade; }
}
