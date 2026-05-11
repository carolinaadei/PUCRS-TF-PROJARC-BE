package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.math.BigDecimal;

/**
 * Porta de saída para o serviço de descontos.
 * Projetada para facilitar a troca da política de fidelidade conforme o enunciado.
 */
public interface IDescontoService {
    /**
     * Calcula o desconto a ser aplicado sobre o custo dos itens.
     *
     * @param custoItens  valor bruto dos itens do pedido
     * @param pedidosRecentes quantidade de pedidos do cliente nos últimos 20 dias
     * @return valor do desconto (pode ser zero)
     */
    BigDecimal calcular(BigDecimal custoItens, int pedidosRecentes);
}
