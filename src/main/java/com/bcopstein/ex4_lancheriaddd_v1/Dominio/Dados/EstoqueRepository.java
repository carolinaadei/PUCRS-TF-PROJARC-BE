package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import java.util.Optional;

public interface EstoqueRepository {
    Optional<ItemEstoque> findByIngredienteId(Long ingredienteId);
    ItemEstoque save(ItemEstoque itemEstoque);
}
