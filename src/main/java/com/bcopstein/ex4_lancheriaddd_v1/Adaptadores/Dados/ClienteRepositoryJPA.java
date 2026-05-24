package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.ClienteEntity;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ClienteJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClienteRepositoryJPA implements ClienteRepository {
    private final ClienteJpaRepository jpa;

    @Autowired
    public ClienteRepositoryJPA(ClienteJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Cliente> recuperaPorCpf(String cpf) {
        return jpa.findById(cpf).map(this::toDomain);
    }

    @Override
    public List<Cliente> recuperaTodos() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity entity = toEntity(cliente);
        return toDomain(jpa.save(entity));
    }

    private Cliente toDomain(ClienteEntity e) {
        return new Cliente(e.getCpf(), e.getNome(), e.getCelular(), e.getEndereco(), e.getEmail());
    }

    private ClienteEntity toEntity(Cliente c) {
        return new ClienteEntity(c.getCpf(), c.getNome(), c.getCelular(), c.getEndereco(), c.getEmail());
    }
}
