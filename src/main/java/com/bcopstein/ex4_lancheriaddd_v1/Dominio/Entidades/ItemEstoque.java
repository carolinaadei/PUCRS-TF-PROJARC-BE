package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itens_estoque")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingrediente_id", nullable = false, unique = true)
    private Ingrediente ingrediente;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidade;

    public boolean temSuficiente(BigDecimal qtd) {
        return this.quantidade.compareTo(qtd) >= 0;
    }

    public void consumir(BigDecimal qtd) {
        this.quantidade = this.quantidade.subtract(qtd);
    }
}
