package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_cpf", nullable = false)
    private ClienteEntity cliente;

    private LocalDateTime dataHoraPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pedido.Status status;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private Double impostos;

    @Column(nullable = false)
    private Double desconto;

    @Column(nullable = false)
    private Double valorCobrado;

    private String enderecoEntrega;
    private String canceladoPor;
    private LocalDateTime dataHoraCancelamento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoEntity> itens;

    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
    private List<PedidoStatusHistoricoEntity> statusHistorico;
}
