package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ReceitaEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ReceitaIngredienteEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PorcaoIngrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;

import java.util.List;

/** Converte ReceitaEntity (JPA) em Receita (domínio), incluindo a porção de cada ingrediente. */
public final class ReceitaMapper {

    private ReceitaMapper() {}

    public static Receita toDomain(ReceitaEntity e) {
        List<Ingrediente> ingredientes = e.getIngredientes().stream()
            .map(i -> new Ingrediente(i.getId(), i.getDescricao()))
            .toList();
        Receita receita = new Receita(e.getId(), e.getTitulo(), ingredientes);

        List<PorcaoIngrediente> porcoes = e.getPorcoes() == null ? List.of() : e.getPorcoes().stream()
            .map(ReceitaMapper::toPorcao)
            .toList();
        receita.setPorcoes(porcoes);

        return receita;
    }

    private static PorcaoIngrediente toPorcao(ReceitaIngredienteEntity ri) {
        Ingrediente ingrediente = new Ingrediente(ri.getIngrediente().getId(), ri.getIngrediente().getDescricao());
        return new PorcaoIngrediente(ingrediente, ri.getQuantidade());
    }
}
