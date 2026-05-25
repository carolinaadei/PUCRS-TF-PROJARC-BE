package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ReceitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceitaJpaRepository extends JpaRepository<ReceitaEntity, Long> {
}
