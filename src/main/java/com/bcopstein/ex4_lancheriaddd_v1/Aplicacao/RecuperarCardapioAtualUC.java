package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;


@Component
public class RecuperarCardapioAtualUC {
    //private RecuperarCardapioUC recuperarCardapioUC;

    @Autowired
    private RecuperarCardapioUC recuperarCardapioUC;

    public CardapioResponse run() {
        long idCardapioAtual = 1; // Supondo ID do cardápio atual seja 1
        return recuperarCardapioUC.run(idCardapioAtual);
    }
}
