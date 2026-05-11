package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.math.BigDecimal;

/**
 * Porta de saída para o serviço de impostos.
 * Projetada para facilitar a troca da política de cálculo conforme o enunciado.
 */
public interface IImpostoService {
    /**
     * Calcula o imposto sobre o valor líquido (após desconto).
     *
     * @param valorLiquido valor base para incidência do imposto
     * @return valor do imposto a ser somado
     */
    BigDecimal calcular(BigDecimal valorLiquido);
}
