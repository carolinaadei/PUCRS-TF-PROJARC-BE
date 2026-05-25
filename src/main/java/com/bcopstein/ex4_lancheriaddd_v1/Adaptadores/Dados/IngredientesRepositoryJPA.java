package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.IngredienteJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ReceitaJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.IngredientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class IngredientesRepositoryJPA implements IngredientesRepository {
    private final IngredienteJpaRepository ingredienteJpa;
    private final ReceitaJpaRepository receitaJpa;

    @Autowired
    public IngredientesRepositoryJPA(IngredienteJpaRepository ingredienteJpa,
                                     ReceitaJpaRepository receitaJpa) {
        this.ingredienteJpa = ingredienteJpa;
        this.receitaJpa = receitaJpa;
    }

    @Override
    public List<Ingrediente> recuperaTodos() {
        return ingredienteJpa.findAll().stream()
            .map(e -> new Ingrediente(e.getId(), e.getDescricao()))
            .toList();
    }

    @Override
    public List<Ingrediente> recuperaIngredientesReceita(long receitaId) {
        return receitaJpa.findById(receitaId)
            .map(r -> r.getIngredientes().stream()
                .map(e -> new Ingrediente(e.getId(), e.getDescricao()))
                .toList())
            .orElse(List.of());
    }
}
