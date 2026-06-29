package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReceitaIngredienteId implements Serializable {
    private Long receitaId;
    private Long ingredienteId;
}
