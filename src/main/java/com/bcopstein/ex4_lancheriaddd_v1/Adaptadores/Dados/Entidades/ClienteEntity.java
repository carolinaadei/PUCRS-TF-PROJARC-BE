package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {
    @Id
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String celular;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String email;
}
