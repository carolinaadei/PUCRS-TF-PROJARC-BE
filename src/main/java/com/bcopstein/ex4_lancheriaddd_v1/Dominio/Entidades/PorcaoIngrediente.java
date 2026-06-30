package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

/** Quantidade (porção) de um ingrediente necessária para preparar uma unidade de um produto. */
public class PorcaoIngrediente {

    private final Ingrediente ingrediente;
    private final int quantidade;

    public PorcaoIngrediente(Ingrediente ingrediente, int quantidade) {
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
