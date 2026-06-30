package com.pizzaria.estoque.repository;

import com.pizzaria.estoque.entity.ItemEstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemEstoqueJpaRepository extends JpaRepository<ItemEstoqueEntity, Long> {
    Optional<ItemEstoqueEntity> findByIngredienteId(Long ingredienteId);
}
