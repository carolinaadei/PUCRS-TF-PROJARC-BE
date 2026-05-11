package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredientes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(nullable = false, length = 20)
    private String unidade;
}
