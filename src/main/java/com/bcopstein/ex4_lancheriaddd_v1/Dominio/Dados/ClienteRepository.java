package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

import java.util.Optional;

public interface ClienteRepository {

    Cliente cadastrar(Cliente cliente);

    boolean existePorEmail(String email);

    boolean existePorCpf(String cpf);

    Optional<Cliente> buscarPorEmail(String email);
}