package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Produto {

    private long id;
    private String descricao;
    private Receita receita;
    private int preco;
    private boolean disponivel = true;

    public Produto(long id, String descricao, Receita receita, int preco) {
        this(id, descricao, receita, preco, true);
    }

    public Produto(long id, String descricao, Receita receita, int preco, boolean disponivel) {
        if (!Produto.precoValido(preco))
            throw new IllegalArgumentException("Preco invalido: " + preco);
        if (descricao == null || descricao.length() == 0)
            throw new IllegalArgumentException("Descricao invalida");
        this.id = id;
        this.descricao = descricao;
        this.receita = receita;
        this.preco = preco;
        this.disponivel = disponivel;
    }

    public long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Receita getReceita() {
        return receita;
    }

    public int getPreco() {
        return preco;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setPreco(int preco) {
        if (!Produto.precoValido(preco))
            throw new IllegalArgumentException("Preco invalido: " + preco);
        this.preco = preco;
    }

    public static boolean precoValido(int preco) {
        return preco > 0;
    }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", descricao=" + descricao + ", receita=" + receita + ", preco=" + preco + "]";
    }
}
