package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

public interface IImpostoService {
    String getLei();
    double calcular(double valorBase);
}
