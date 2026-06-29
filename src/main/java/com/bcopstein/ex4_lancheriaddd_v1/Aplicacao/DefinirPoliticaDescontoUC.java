package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ConfiguracaoDesconto;

@Component
public class DefinirPoliticaDescontoUC implements IDefinirPoliticaDescontoUC {

    private final ConfiguracaoDesconto configuracaoDesconto;

    @Autowired
    public DefinirPoliticaDescontoUC(ConfiguracaoDesconto configuracaoDesconto) {
        this.configuracaoDesconto = configuracaoDesconto;
    }

    @Override
    public String run(String codigo) {
        configuracaoDesconto.trocarPolitica(codigo);
        return configuracaoDesconto.getPoliticaCorrente().getCodigo();
    }
}
