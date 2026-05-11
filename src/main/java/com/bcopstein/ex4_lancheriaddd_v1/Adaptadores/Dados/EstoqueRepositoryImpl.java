package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

interface EstoqueJpaRepository extends JpaRepository<ItemEstoque, Long> {
    Optional<ItemEstoque> findByIngredienteId(Long ingredienteId);
}

@Repository
@RequiredArgsConstructor
class EstoqueRepositoryImpl implements EstoqueRepository {

    private final EstoqueJpaRepository jpa;

    @Override
    public Optional<ItemEstoque> findByIngredienteId(Long ingredienteId) {
        return jpa.findByIngredienteId(ingredienteId);
    }

    @Override
    public ItemEstoque save(ItemEstoque item) {
        return jpa.save(item);
    }
}
