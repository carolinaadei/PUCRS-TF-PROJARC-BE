package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementação atual:
 * - Clientes com mais de 3 pedidos nos últimos 20 dias recebem 7% de desconto.
 * A interface IDescontoService permite trocar a política sem alterar os use cases.
 */
@Service
public class DescontoService implements IDescontoService {

    private static final int PEDIDOS_PARA_DESCONTO = 3;
    private static final BigDecimal PERCENTUAL_DESCONTO = new BigDecimal("0.07");

    @Override
    public BigDecimal calcular(BigDecimal custoItens, int pedidosRecentes) {
        if (pedidosRecentes > PEDIDOS_PARA_DESCONTO) {
            return custoItens.multiply(PERCENTUAL_DESCONTO).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
