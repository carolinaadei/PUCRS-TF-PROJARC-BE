package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ConfiguracaoCardapio;

@Component
public class RecuperarCardapioAtualUC {

    @Autowired
    private RecuperarCardapioUC recuperarCardapioUC;

    @Autowired
    private ConfiguracaoCardapio configuracaoCardapio;

    /**
     * Retorna o cardápio corrente com descrição e preço dos itens.
     * O cardápio corrente é definido pela ConfiguracaoCardapio.
     */
    public CardapioResponse run() {
        long idCardapioAtual = configuracaoCardapio.getIdCardapioAtual();
        return recuperarCardapioUC.run(idCardapioAtual);
    }
}