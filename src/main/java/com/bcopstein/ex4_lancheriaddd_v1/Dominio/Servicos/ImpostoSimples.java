package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

@Service
public class ImpostoSimples implements IImpostoService {

    private static final double TAXA = 0.10;

    @Override
    public double calcular(double valorBase) {
        return valorBase * TAXA;
    }
}
