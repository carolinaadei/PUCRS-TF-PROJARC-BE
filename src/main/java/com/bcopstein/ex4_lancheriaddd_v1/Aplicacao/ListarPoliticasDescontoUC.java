package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ConfiguracaoDesconto;

@Component
public class ListarPoliticasDescontoUC implements IListarPoliticasDescontoUC {

    private final ConfiguracaoDesconto configuracaoDesconto;

    @Autowired
    public ListarPoliticasDescontoUC(ConfiguracaoDesconto configuracaoDesconto) {
        this.configuracaoDesconto = configuracaoDesconto;
    }

    @Override
    public List<String> run() {
        return configuracaoDesconto.getTodasPoliticas().stream()
            .map(p -> p.getCodigo())
            .toList();
    }
}
