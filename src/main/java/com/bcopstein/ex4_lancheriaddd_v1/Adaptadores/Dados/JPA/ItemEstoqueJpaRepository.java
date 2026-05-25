package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ItemEstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemEstoqueJpaRepository extends JpaRepository<ItemEstoqueEntity, Long> {
    Optional<ItemEstoqueEntity> findByIngredienteId(Long ingredienteId);
}
