package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "receitas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaEntity {
    @Id
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @OneToMany(mappedBy = "receita", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceitaIngredienteEntity> porcoes;

    /** Compatibilidade: lista simples de ingredientes, sem a porção necessária. */
    public List<IngredienteEntity> getIngredientes() {
        if (porcoes == null) {
            return List.of();
        }
        return porcoes.stream().map(ReceitaIngredienteEntity::getIngrediente).toList();
    }
}
