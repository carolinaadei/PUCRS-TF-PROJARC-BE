package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ConfiguracaoCardapio;

@Component
public class DefinirCardapioAtualUC implements IDefinirCardapioAtualUC {

    private final ConfiguracaoCardapio configuracaoCardapio;
    private final CardapioService cardapioService;

    @Autowired
    public DefinirCardapioAtualUC(ConfiguracaoCardapio configuracaoCardapio, CardapioService cardapioService) {
        this.configuracaoCardapio = configuracaoCardapio;
        this.cardapioService = cardapioService;
    }

    @Override
    public long run(long idCardapio) {
        cardapioService.recuperaCardapio(idCardapio);
        configuracaoCardapio.setIdCardapioAtual(idCardapio);
        return idCardapio;
    }
}
