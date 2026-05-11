package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import java.util.List;

/**
 * Porta de saída para o serviço de estoque.
 * Permite implementação fake (sempre aprova) e implementação real.
 */
public interface IEstoqueService {

    record ResultadoVerificacao(boolean aprovado, List<Long> itensSemEstoque) {}

    /**
     * Verifica se há ingredientes suficientes para atender todos os itens do pedido.
     * Caso aprovado, desconta os ingredientes do estoque.
     *
     * @param itens lista de itens do pedido
     * @return resultado indicando aprovação e quais itens faltam (se algum)
     */
    ResultadoVerificacao verificarEDescontar(List<ItemPedido> itens);
}
