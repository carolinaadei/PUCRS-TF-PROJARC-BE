package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

public class ImpostoSimples implements IImpostoService {

    public static final String ID = "LC 123/2006";
    private static final double TAXA = 0.10;

    @Override
    public String getLei() {
        return ID;
    }

    @Override
    public double calcular(double valorBase) {
        return valorBase * TAXA;
    }
}
