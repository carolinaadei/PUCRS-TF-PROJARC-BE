package com.pizzaria.estoque.service;

import com.pizzaria.estoque.dto.*;
import com.pizzaria.estoque.entity.ItemEstoqueEntity;
import com.pizzaria.estoque.repository.ItemEstoqueJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    private final ItemEstoqueJpaRepository repository;

    public EstoqueService(ItemEstoqueJpaRepository repository) {
        this.repository = repository;
    }

    public List<ItemEstoqueResponse> listarTodos() {
        return repository.findAll().stream()
            .map(e -> new ItemEstoqueResponse(
                e.getIngrediente().getId(),
                e.getIngrediente().getDescricao(),
                e.getQuantidade()))
            .toList();
    }

    @Transactional
    public VerificacaoResponse verificarEConsumir(VerificacaoRequest request) {
        List<Long> indisponiveis = new ArrayList<>();

        for (IngredienteQuantidadeDTO iq : request.ingredientes()) {
            Optional<ItemEstoqueEntity> optItem = repository.findByIngredienteId(iq.ingredienteId());
            if (optItem.isEmpty() || optItem.get().getQuantidade() < iq.quantidade()) {
                indisponiveis.add(iq.ingredienteId());
            }
        }

        if (!indisponiveis.isEmpty()) {
            return new VerificacaoResponse(false, indisponiveis);
        }

        for (IngredienteQuantidadeDTO iq : request.ingredientes()) {
            ItemEstoqueEntity item = repository.findByIngredienteId(iq.ingredienteId()).get();
            item.setQuantidade(item.getQuantidade() - iq.quantidade());
            repository.save(item);
        }

        return new VerificacaoResponse(true, List.of());
    }

    @Transactional
    public void atualizarQuantidade(Long ingredienteId, Integer novaQuantidade) {
        ItemEstoqueEntity item = repository.findByIngredienteId(ingredienteId)
            .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado no estoque: " + ingredienteId));
        item.setQuantidade(novaQuantidade);
        repository.save(item);
    }
}
