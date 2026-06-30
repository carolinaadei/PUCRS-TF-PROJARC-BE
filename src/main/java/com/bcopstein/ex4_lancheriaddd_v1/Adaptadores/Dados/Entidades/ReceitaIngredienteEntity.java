package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receita_ingrediente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceitaIngredienteEntity {

    @EmbeddedId
    private ReceitaIngredienteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receitaId")
    @JoinColumn(name = "receita_id")
    private ReceitaEntity receita;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("ingredienteId")
    @JoinColumn(name = "ingrediente_id")
    private IngredienteEntity ingrediente;

    @Column(nullable = false)
    private Integer quantidade;

    public ReceitaIngredienteEntity(ReceitaEntity receita, IngredienteEntity ingrediente, Integer quantidade) {
        this.id = new ReceitaIngredienteId(receita.getId(), ingrediente.getId());
        this.receita = receita;
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }
}
