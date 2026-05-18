package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ReceitaEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ReceitaJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ReceitasRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class ReceitasRepositoryJPA implements ReceitasRepository {
    private final ReceitaJpaRepository receitaJpa;

    @Autowired
    public ReceitasRepositoryJPA(ReceitaJpaRepository receitaJpa) {
        this.receitaJpa = receitaJpa;
    }

    @Override
    public Receita recuperaReceita(long id) {
        return receitaJpa.findById(id).map(this::toDomain).orElse(null);
    }

    private Receita toDomain(ReceitaEntity e) {
        List<Ingrediente> ingredientes = e.getIngredientes().stream()
            .map(i -> new Ingrediente(i.getId(), i.getDescricao()))
            .toList();
        return new Receita(e.getId(), e.getTitulo(), ingredientes);
    }
}
