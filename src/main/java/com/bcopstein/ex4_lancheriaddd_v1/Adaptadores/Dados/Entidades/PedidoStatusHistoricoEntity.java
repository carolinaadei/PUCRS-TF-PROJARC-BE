package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_status_historico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoStatusHistoricoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pedido.Status status;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String responsavel;
}
