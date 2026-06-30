package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Component
public class SemDesconto implements DescontoPolicy {

    public static final String CODIGO = "SemDesconto";

    @Override
    public String getCodigo() {
        return CODIGO;
    }

    @Override
    public double calcular(List<ItemPedido> itens) {
        return 0.0;
    }
}
