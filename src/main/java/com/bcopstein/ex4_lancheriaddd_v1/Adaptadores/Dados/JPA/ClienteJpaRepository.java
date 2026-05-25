package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByCpf(String cpf);
    Optional<ClienteEntity> findByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
