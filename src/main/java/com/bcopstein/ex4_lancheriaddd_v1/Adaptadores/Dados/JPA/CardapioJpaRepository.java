package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.CardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardapioJpaRepository extends JpaRepository<CardapioEntity, Long> {
}
