package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import java.util.List;
import java.util.Optional;

public interface EstoqueRepository {
    List<ItemEstoque> recuperaTodos();
    Optional<ItemEstoque> recuperaPorIngredienteId(long ingredienteId);
    void atualizar(ItemEstoque item);
}
