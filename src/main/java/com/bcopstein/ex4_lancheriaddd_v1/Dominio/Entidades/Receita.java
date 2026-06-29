package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.List;

public class Receita {

    private long id;
    private String titulo;
    private List<Ingrediente> ingredientes;
    private List<PorcaoIngrediente> porcoes;

    public Receita(long id, String titulo, List<Ingrediente> ingredientes) {
        this.id = id;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setPorcoes(List<PorcaoIngrediente> porcoes) {
        this.porcoes = porcoes;
    }

    /**
     * Porção (quantidade) de cada ingrediente necessária por unidade do produto.
     * Quando não informada explicitamente (ex.: construído sem dados de porção),
     * assume 1 porção por ingrediente para preservar o comportamento anterior.
     */
    public List<PorcaoIngrediente> getPorcoes() {
        if (porcoes != null) {
            return porcoes;
        }
        if (ingredientes == null) {
            return List.of();
        }
        return ingredientes.stream().map(i -> new PorcaoIngrediente(i, 1)).toList();
    }
}