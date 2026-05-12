package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Component;

/**
 * Mantém a configuração do cardápio corrente.
 * Atualmente o cardápio padrão é fixo (id=1), mas esta classe
 * permite que o gerente altere o cardápio corrente futuramente
 * sem modificar a lógica de negócio.
 */
@Component
public class ConfiguracaoCardapio {
    private long idCardapioAtual = 1;

    /**
     * Retorna o id do cardápio corrente.
     */
    public long getIdCardapioAtual() {
        return idCardapioAtual;
    }

    /**
     * Permite ao gerente alterar o cardápio corrente.
     */
    public void setIdCardapioAtual(long idCardapioAtual) {
        this.idCardapioAtual = idCardapioAtual;
    }
}