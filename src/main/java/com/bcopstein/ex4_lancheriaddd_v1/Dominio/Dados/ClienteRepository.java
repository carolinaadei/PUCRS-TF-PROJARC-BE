package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Optional<Cliente> recuperaPorCpf(String cpf);

    List<Cliente> recuperaTodos();

    Cliente salvar(Cliente cliente);

    Cliente buscarPorCpf(String cpf);
}
