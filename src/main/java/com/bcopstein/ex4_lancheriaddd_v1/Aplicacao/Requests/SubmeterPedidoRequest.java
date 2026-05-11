package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SubmeterPedidoRequest(
        @NotBlank(message = "Endereço de entrega é obrigatório")
        String enderecoEntrega,

        @NotEmpty(message = "O pedido deve conter ao menos um item")
        @Valid
        List<ItemPedidoRequest> itens
) {
    public record ItemPedidoRequest(
            Long itemCardapioId,
            int quantidade
    ) {}
}
