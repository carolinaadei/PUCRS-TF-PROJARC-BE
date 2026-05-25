package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {

    public enum Status {
        NOVO,
        APROVADO,
        PAGO,
        AGUARDANDO,
        PREPARACAO,
        PRONTO,
        TRANSPORTE,
        ENTREGUE,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_cpf", nullable = false)
    private Cliente cliente;

    @Column(name = "data_hora_pagamento")
    private LocalDateTime dataHoraPagamento;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "itens_pedido", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<ItemPedido> itens;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "valor")
    private double valor;

    @Column(name = "impostos")
    private double impostos;

    @Column(name = "desconto")
    private double desconto;

    @Column(name = "valor_cobrado")
    private double valorCobrado;

    @Column(name = "cancelado_por")
    private String canceladoPor;

    @Column(name = "data_hora_cancelamento")
    private LocalDateTime dataHoraCancelamento;

    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

    // Construtor vazio obrigatório para JPA
    protected Pedido() {
    }

    // Construtor completo (inclui enderecoEntrega)
    public Pedido(long id, Cliente cliente, LocalDateTime dataHoraPagamento,
            List<ItemPedido> itens, Status status,
            double valor, double impostos, double desconto, double valorCobrado,
            String enderecoEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.dataHoraPagamento = dataHoraPagamento;
        this.itens = itens;
        this.status = status;
        this.valor = valor;
        this.impostos = impostos;
        this.desconto = desconto;
        this.valorCobrado = valorCobrado;
        this.enderecoEntrega = enderecoEntrega;
    }

    // Construtor de compatibilidade (sem enderecoEntrega)
    public Pedido(long id, Cliente cliente, LocalDateTime dataHoraPagamento,
            List<ItemPedido> itens, Status status,
            double valor, double impostos, double desconto, double valorCobrado) {
        this(id, cliente, dataHoraPagamento, itens, status,
                valor, impostos, desconto, valorCobrado, null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getValor() {
        return valor;
    }

    public double getImpostos() {
        return impostos;
    }

    public double getDesconto() {
        return desconto;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    public String getCanceladoPor() {
        return canceladoPor;
    }

    public void setCanceladoPor(String canceladoPor) {
        this.canceladoPor = canceladoPor;
    }

    public LocalDateTime getDataHoraCancelamento() {
        return dataHoraCancelamento;
    }

    public void setDataHoraCancelamento(LocalDateTime dataHoraCancelamento) {
        this.dataHoraCancelamento = dataHoraCancelamento;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
}