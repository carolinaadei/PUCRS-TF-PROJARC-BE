package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemCardapio;
import java.math.BigDecimal;
import java.util.List;

public record CardapioResponse(
        Long id,
        String descricao,
        List<ItemCardapioResponse> itens
) {
    public record ItemCardapioResponse(
            Long id,
            String descricao,
            BigDecimal precoUnit,
            boolean disponivel
    ) {
        public static ItemCardapioResponse from(ItemCardapio ic) {
            return new ItemCardapioResponse(ic.getId(), ic.getDescricao(), ic.getPrecoUnit(), ic.isDisponivel());
        }
    }

    public static CardapioResponse from(Cardapio c) {
        List<ItemCardapioResponse> itens = c.getItens() == null
                ? List.of()
                : c.getItens().stream()
                        .filter(ItemCardapio::isDisponivel)
                        .map(ItemCardapioResponse::from)
                        .toList();
        return new CardapioResponse(c.getId(), c.getDescricao(), itens);
    }
}
