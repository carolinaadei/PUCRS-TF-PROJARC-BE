package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "celular")
    private String celular;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "email")
    private String email;

    // Construtor vazio obrigatório para JPA
    protected Cliente() {
    }

    public Cliente(String cpf, String nome, String celular, String endereco, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.celular = celular;
        this.endereco = endereco;
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
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
}