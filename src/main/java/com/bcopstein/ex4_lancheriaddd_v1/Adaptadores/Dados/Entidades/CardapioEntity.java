package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cardapios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardapioEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cardapio_produto",
        joinColumns = @JoinColumn(name = "cardapio_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<ProdutoEntity> produtos;
}
