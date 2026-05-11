package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Column(name = "endereco_entrega", nullable = false, length = 300)
    private String enderecoEntrega;

    @Column(name = "custo_itens", precision = 10, scale = 2)
    private BigDecimal custoItens;

    @Column(precision = 10, scale = 2)
    private BigDecimal desconto;

    @Column(precision = 10, scale = 2)
    private BigDecimal imposto;

    @Column(name = "custo_final", precision = 10, scale = 2)
    private BigDecimal custoFinal;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    /** Regra de negócio: só pode cancelar pedidos APROVADOS (não pagos). */
    public boolean podeCancelar() {
        return this.status == StatusPedido.APROVADO;
    }

    /** Regra de negócio: só pode pagar pedidos APROVADOS. */
    public boolean podePagar() {
        return this.status == StatusPedido.APROVADO;
    }
}
