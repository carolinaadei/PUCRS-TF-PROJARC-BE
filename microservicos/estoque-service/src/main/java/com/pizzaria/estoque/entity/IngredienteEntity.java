package com.pizzaria.estoque.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredienteEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String descricao;
}
