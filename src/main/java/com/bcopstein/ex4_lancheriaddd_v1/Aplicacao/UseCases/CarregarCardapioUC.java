package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.UseCases;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;

/**
 * UC3 - Carregar cardápio corrente (requer autenticação).
 */
public interface CarregarCardapioUC {
    CardapioResponse executar();
}
