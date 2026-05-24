package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

public interface DescontoPolicy {
    double calcular(List<ItemPedido> itens);
}
