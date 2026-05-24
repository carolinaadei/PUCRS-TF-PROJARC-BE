package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String descricao;

    private Long preco;

    @Column(name = "indicacao_chef", nullable = false)
    private boolean indicacaoChef;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "produto_receita",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "receita_id")
    )
    private List<ReceitaEntity> receitas;
}
