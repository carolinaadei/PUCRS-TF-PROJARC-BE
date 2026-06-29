package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImpostoServiceTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("ImpostoSimples tem id LC 123/2006")
    void simplesId() {
        assertEquals("LC 123/2006", new ImpostoSimples().getLei());
    }

    @Test
    @DisplayName("ImpostoSimples aplica 10%")
    void simplesCalculo() {
        assertEquals(100.0, new ImpostoSimples().calcular(1000.0), DELTA);
    }

    @Test
    @DisplayName("ImpostoLucroPresumido tem id Lei 9.249/1995")
    void lucroPresumidoId() {
        assertEquals("Lei 9.249/1995", new ImpostoLucroPresumido().getLei());
    }

    @Test
    @DisplayName("ImpostoLucroPresumido aplica 15%")
    void lucroPresumidoCalculo() {
        assertEquals(150.0, new ImpostoLucroPresumido().calcular(1000.0), DELTA);
    }
}
