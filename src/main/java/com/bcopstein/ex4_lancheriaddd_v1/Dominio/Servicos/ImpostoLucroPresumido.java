package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

public class ImpostoLucroPresumido implements IImpostoService {

    public static final String ID = "Lei 9.249/1995";
    private static final double TAXA = 0.15;

    @Override
    public String getLei() {
        return ID;
    }

    @Override
    public double calcular(double valorBase) {
        return valorBase * TAXA;
    }
}
