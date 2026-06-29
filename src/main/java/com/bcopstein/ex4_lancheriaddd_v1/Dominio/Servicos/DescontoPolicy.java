package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

public interface DescontoPolicy {
    String getCodigo();
    double calcular(List<ItemPedido> itens);

    default boolean seAplica(String clienteCpf) {
        return true;
    }
}
