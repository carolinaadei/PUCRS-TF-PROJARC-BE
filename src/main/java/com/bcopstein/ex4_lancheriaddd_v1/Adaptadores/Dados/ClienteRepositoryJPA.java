package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ClienteEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ClienteJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepositoryJPA implements ClienteRepository {

    private final ClienteJpaRepository clienteJpa;

    @Autowired
    public ClienteRepositoryJPA(ClienteJpaRepository clienteJpa) {
        this.clienteJpa = clienteJpa;
    }

    @Override
    public Cliente cadastrar(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setNome(cliente.getNome());
        entity.setCpf(cliente.getCpf());
        entity.setCelular(cliente.getCelular());
        entity.setEndereco(cliente.getEndereco());
        entity.setEmail(cliente.getEmail());
        entity.setSenhaHash(cliente.getSenhaHash());
        ClienteEntity saved = clienteJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existePorEmail(String email) {
        return clienteJpa.existsByEmail(email);
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return clienteJpa.existsByCpf(cpf);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteJpa.findByEmail(email).map(this::toDomain);
    }

    private Cliente toDomain(ClienteEntity e) {
        return new Cliente(e.getId(), e.getNome(), e.getCpf(),
                e.getCelular(), e.getEndereco(), e.getEmail(), e.getSenhaHash());
    }
}
