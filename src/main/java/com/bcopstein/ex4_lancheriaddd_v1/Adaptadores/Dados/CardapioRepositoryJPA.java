package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ProdutoEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ReceitaEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.CardapioJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ProdutoJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class CardapioRepositoryJPA implements CardapioRepository {

    private final CardapioJpaRepository cardapioJpa;
    private final ProdutoJpaRepository produtoJpa;

    @Autowired
    public CardapioRepositoryJPA(CardapioJpaRepository cardapioJpa,
                                  ProdutoJpaRepository produtoJpa) {
        this.cardapioJpa = cardapioJpa;
        this.produtoJpa = produtoJpa;
    }

    @Override
    public List<CabecalhoCardapio> cardapiosDisponiveis() {
        return cardapioJpa.findAll().stream()
            .map(e -> new CabecalhoCardapio(e.getId(), e.getTitulo()))
            .toList();
    }

    @Override
    public Cardapio recuperaPorId(long id) {
        return cardapioJpa.findById(id).map(e -> {
            CabecalhoCardapio cabecalho = new CabecalhoCardapio(e.getId(), e.getTitulo());
            List<Produto> produtos = e.getProdutos().stream()
                .map(this::produtoToDomain)
                .toList();
            return new Cardapio(cabecalho, produtos);
        }).orElse(null);
    }

    @Override
    public List<Produto> indicacoesDoChef() {
        return produtoJpa.findByIndicacaoChefTrue().stream()
            .map(this::produtoToDomain)
            .toList();
    }

    private Produto produtoToDomain(ProdutoEntity e) {
        Receita receita = null;
        if (e.getReceitas() != null && !e.getReceitas().isEmpty()) {
            ReceitaEntity r = e.getReceitas().get(0);
            List<Ingrediente> ingredientes = r.getIngredientes().stream()
                .map(i -> new Ingrediente(i.getId(), i.getDescricao()))
                .toList();
            receita = new Receita(r.getId(), r.getTitulo(), ingredientes);
        }
        return new Produto(e.getId(), e.getDescricao(), receita, e.getPreco().intValue());
    }
}
