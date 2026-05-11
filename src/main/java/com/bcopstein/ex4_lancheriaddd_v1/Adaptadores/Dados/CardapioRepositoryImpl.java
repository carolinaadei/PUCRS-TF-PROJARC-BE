package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CardapioRepositoryImpl implements CardapioRepository {

    private final CardapioJpaRepository jpa;

    @Override
    public Optional<Cardapio> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Cardapio> findCardapioCorrente() {
        return jpa.findByCorrenteTrue();
    }
}
