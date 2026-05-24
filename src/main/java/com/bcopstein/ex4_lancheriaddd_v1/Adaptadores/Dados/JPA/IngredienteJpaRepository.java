package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.IngredienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteJpaRepository extends JpaRepository<IngredienteEntity, Long> {
}
