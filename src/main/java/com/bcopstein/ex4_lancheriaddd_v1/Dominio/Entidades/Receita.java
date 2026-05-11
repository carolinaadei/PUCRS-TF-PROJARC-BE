package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "receitas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_cardapio_id", nullable = false)
    private ItemCardapio itemCardapio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;
}
