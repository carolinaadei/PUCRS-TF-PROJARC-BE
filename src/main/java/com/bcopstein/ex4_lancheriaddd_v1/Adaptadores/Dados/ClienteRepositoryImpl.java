package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteJpaRepository jpa;

    @Override public Cliente save(Cliente c)                  { return jpa.save(c); }
    @Override public Optional<Cliente> findById(Long id)      { return jpa.findById(id); }
    @Override public Optional<Cliente> findByEmail(String e)  { return jpa.findByEmail(e); }
    @Override public boolean existsByEmail(String e)          { return jpa.existsByEmail(e); }
    @Override public boolean existsByCpf(String cpf)          { return jpa.existsByCpf(cpf); }
}
