package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.IngredienteEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ItemEstoqueEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ItemEstoqueJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EstoqueRepositoryJPA implements EstoqueRepository {
    private final ItemEstoqueJpaRepository jpa;

    @Autowired
    public EstoqueRepositoryJPA(ItemEstoqueJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<ItemEstoque> recuperaTodos() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<ItemEstoque> recuperaPorIngredienteId(long ingredienteId) {
        return jpa.findByIngredienteId(ingredienteId).map(this::toDomain);
    }

    @Override
    public void atualizar(ItemEstoque item) {
        jpa.findByIngredienteId(item.getIngrediente().getId()).ifPresent(entity -> {
            entity.setQuantidade(item.getQuantidade());
            jpa.save(entity);
        });
    }

    private ItemEstoque toDomain(ItemEstoqueEntity e) {
        Ingrediente ingrediente = new Ingrediente(
            e.getIngrediente().getId(),
            e.getIngrediente().getDescricao()
        );
        return new ItemEstoque(ingrediente, e.getQuantidade());
    }
}
