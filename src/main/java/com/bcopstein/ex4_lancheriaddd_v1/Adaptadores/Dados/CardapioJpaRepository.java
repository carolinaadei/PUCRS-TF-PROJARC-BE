package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface CardapioJpaRepository extends JpaRepository<Cardapio, Long> {

    @Query("SELECT c FROM Cardapio c LEFT JOIN FETCH c.itens ic " +
           "LEFT JOIN FETCH ic.receitas r LEFT JOIN FETCH r.ingrediente " +
           "WHERE c.corrente = true")
    Optional<Cardapio> findByCorrenteTrue();
}
