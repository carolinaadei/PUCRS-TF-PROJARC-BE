package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "itensEstoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemEstoqueEntity {
    @Id
    private Long id;

    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingrediente_id")
    private IngredienteEntity ingrediente;
}
