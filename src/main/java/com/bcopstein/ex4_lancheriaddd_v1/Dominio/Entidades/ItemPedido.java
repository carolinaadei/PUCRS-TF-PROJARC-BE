package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_cardapio_id", nullable = false)
    private ItemCardapio itemCardapio;

    @Column(nullable = false)
    private int quantidade;

    /** Snapshot do preço no momento do pedido. */
    @Column(name = "preco_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnit;

    public BigDecimal subtotal() {
        return precoUnit.multiply(BigDecimal.valueOf(quantidade));
    }
}
