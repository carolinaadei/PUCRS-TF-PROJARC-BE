package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        StatusPedido status,
        String enderecoEntrega,
        BigDecimal custoItens,
        BigDecimal desconto,
        BigDecimal imposto,
        BigDecimal custoFinal,
        LocalDateTime criadoEm,
        List<ItemPedidoResponse> itens,
        List<Long> itensSemEstoque   // preenchido quando negado
) {
    public record ItemPedidoResponse(
            Long itemCardapioId,
            String descricao,
            int quantidade,
            BigDecimal precoUnit,
            BigDecimal subtotal
    ) {
        public static ItemPedidoResponse from(ItemPedido ip) {
            return new ItemPedidoResponse(
                    ip.getItemCardapio().getId(),
                    ip.getItemCardapio().getDescricao(),
                    ip.getQuantidade(),
                    ip.getPrecoUnit(),
                    ip.subtotal()
            );
        }
    }

    public static PedidoResponse from(Pedido p) {
        return from(p, List.of());
    }

    public static PedidoResponse from(Pedido p, List<Long> itensSemEstoque) {
        List<ItemPedidoResponse> itens = p.getItens() == null
                ? List.of()
                : p.getItens().stream().map(ItemPedidoResponse::from).toList();
        return new PedidoResponse(
                p.getId(), p.getStatus(), p.getEnderecoEntrega(),
                p.getCustoItens(), p.getDesconto(), p.getImposto(), p.getCustoFinal(),
                p.getCriadoEm(), itens, itensSemEstoque
        );
    }
}
