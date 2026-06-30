package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ProdutoEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.CardapioJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ProdutoJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class ProdutosRepositoryJPA implements ProdutosRepository {
    private final ProdutoJpaRepository produtoJpa;
    private final CardapioJpaRepository cardapioJpa;

    public ProdutosRepositoryJPA(ProdutoJpaRepository produtoJpa,
                                  CardapioJpaRepository cardapioJpa) {
        this.produtoJpa = produtoJpa;
        this.cardapioJpa = cardapioJpa;
    }

    @Override
    public Produto recuperaProdutoPorid(long id) {
        return produtoJpa.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<Produto> recuperaProdutosCardapio(long cardapioId) {
        return cardapioJpa.findById(cardapioId)
            .map(c -> c.getProdutos().stream().map(this::toDomain).toList())
            .orElse(List.of());
    }

    @Override
    @Transactional
    public void atualizarDisponibilidade(long produtoId, boolean disponivel) {
        produtoJpa.findById(produtoId).ifPresent(e -> {
            e.setDisponivel(disponivel);
            produtoJpa.save(e);
        });
    }

    private Produto toDomain(ProdutoEntity e) {
        Receita receita = null;
        if (e.getReceitas() != null && !e.getReceitas().isEmpty()) {
            receita = ReceitaMapper.toDomain(e.getReceitas().get(0));
        }
        return new Produto(e.getId(), e.getDescricao(), receita, e.getPreco().intValue(), e.isDisponivel());
    }
}
