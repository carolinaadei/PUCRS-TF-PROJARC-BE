package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

/**
 * Porta de saída para a entidade Cliente.
 * Necessária para que o PedidoService possa validar se o cliente existe
 * antes de criar um pedido.
 */
public interface ClienteRepository {
    Cliente buscarPorCpf(String cpf);
}
