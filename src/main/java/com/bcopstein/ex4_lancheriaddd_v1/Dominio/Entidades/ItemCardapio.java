package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "itens_cardapio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardapio_id", nullable = false)
    private Cardapio cardapio;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(name = "preco_unit", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnit;

    @Column(nullable = false)
    private boolean disponivel;

    @OneToMany(mappedBy = "itemCardapio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Receita> receitas;
}
