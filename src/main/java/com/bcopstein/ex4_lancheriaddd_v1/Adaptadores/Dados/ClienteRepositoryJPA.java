package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ClienteJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteRepositoryJPA implements ClienteRepository {

    private final ClienteJpaRepository clienteJpa;

    @Autowired
    public ClienteRepositoryJPA(ClienteJpaRepository clienteJpa) {
        this.clienteJpa = clienteJpa;
    }

    @Override
    public Cliente buscarPorCpf(String cpf) {
        return clienteJpa.findById(cpf)
                .map(e -> new Cliente(e.getCpf(), e.getNome(), e.getCelular(),
                        e.getEndereco(), e.getEmail()))
                .orElse(null);
    }
}
