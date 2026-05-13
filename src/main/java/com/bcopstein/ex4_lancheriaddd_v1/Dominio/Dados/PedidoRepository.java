package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;
import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido buscarPorId(long id);
    void salvar(Pedido pedido);
    List<Pedido> buscarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim);

}