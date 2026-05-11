package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementação atual: imposto único de 10% sobre o valor líquido.
 * A interface IImpostoService garante que a política pode ser trocada
 * sem alterar nenhum use case.
 */
@Service
public class ImpostoService implements IImpostoService {

    private static final BigDecimal TAXA = new BigDecimal("0.10");

    @Override
    public BigDecimal calcular(BigDecimal valorLiquido) {
        return valorLiquido.multiply(TAXA).setScale(2, RoundingMode.HALF_UP);
    }
}
