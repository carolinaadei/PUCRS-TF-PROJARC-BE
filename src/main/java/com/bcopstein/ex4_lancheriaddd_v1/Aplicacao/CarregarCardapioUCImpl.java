package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.UseCases.CarregarCardapioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UC3 - Retorna o cardápio corrente (marcado com corrente=true no banco).
 */
@Service
@RequiredArgsConstructor
public class CarregarCardapioUCImpl implements CarregarCardapioUC {

    private final CardapioRepository cardapioRepository;

    @Override
    @Transactional(readOnly = true)
    public CardapioResponse executar() {
        Cardapio cardapio = cardapioRepository.findCardapioCorrente()
                .orElseThrow(() -> new IllegalStateException("Nenhum cardápio corrente configurado."));
        return CardapioResponse.from(cardapio);
    }
}
