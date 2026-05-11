package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cardapios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String descricao;

    @Column(nullable = false)
    private boolean corrente;

    @OneToMany(mappedBy = "cardapio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemCardapio> itens;
}
