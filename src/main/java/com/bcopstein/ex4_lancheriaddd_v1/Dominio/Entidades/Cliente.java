package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Cliente {

    private Long id;
    private String nome;
    private String cpf;
    private String celular;
    private String endereco;
    private String email;
    private String senhaHash;

    public Cliente(Long id,
                   String nome,
                   String cpf,
                   String celular,
                   String endereco,
                   String email,
                   String senhaHash) {

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.celular = celular;
        this.endereco = endereco;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCelular() {
        return celular;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }
}