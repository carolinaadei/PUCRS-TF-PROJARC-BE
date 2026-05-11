package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import java.util.Optional;

public interface CardapioRepository {
    Optional<Cardapio> findById(Long id);
    Optional<Cardapio> findCardapioCorrente();
}
