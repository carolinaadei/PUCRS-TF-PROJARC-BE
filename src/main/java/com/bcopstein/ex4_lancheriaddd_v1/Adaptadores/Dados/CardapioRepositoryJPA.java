package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ClienteEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ClienteJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepositoryJPA implements ClienteRepository {

    private final ClienteJpaRepository repository;

    public ClienteRepositoryJPA(ClienteJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente cadastrar(Cliente cliente) {
        ClienteEntity entity = toEntity(cliente);
        ClienteEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toDomain);
    }

    private ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getCelular(),
                cliente.getEndereco(),
                cliente.getEmail(),
                cliente.getSenhaHash()
        );
    }

    private Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getCelular(),
                entity.getEndereco(),
                entity.getEmail(),
                entity.getSenhaHash()
        );
    }
}