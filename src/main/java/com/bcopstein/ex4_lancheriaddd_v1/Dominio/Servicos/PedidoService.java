package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

/**
 * Serviço de domínio responsável pelas operações sobre Pedido.
 *
 * Regras de negócio implementadas aqui:
 *  - O cliente deve existir no cadastro.
 *  - O pedido deve conter ao menos um item.
 *  - Cada item deve ter quantidade >= 1.
 *  - O endereço de entrega não pode ser vazio.
 *  - Apenas pedidos com status NOVO ou APROVADO podem ser cancelados.
 *
 * Esta classe SUBSTITUI o PedidoService já existente no projeto,
 * unificando submissão e cancelamento em um único serviço de domínio.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    // ─────────────────────────────────────────────────────────
    // SUBMETER PEDIDO — cria um pedido com status NOVO
    // ─────────────────────────────────────────────────────────

    /**
     * Cria um novo pedido com status NOVO após validar cliente, endereço e itens.
     *
     * @param clienteCpf      CPF do cliente que está fazendo o pedido
     * @param enderecoEntrega Endereço onde o pedido deve ser entregue
     * @param itens           Lista de itens com produto e quantidade
     * @return Pedido criado e persistido com ID gerado
     */
    public Pedido submeter(String clienteCpf, String enderecoEntrega, List<ItemPedido> itens) {
        // ── Validações de domínio ──────────────────────────────
        if (clienteCpf == null || clienteCpf.isBlank()) {
            throw new IllegalArgumentException("CPF do cliente é obrigatório");
        }
        if (enderecoEntrega == null || enderecoEntrega.isBlank()) {
            throw new IllegalArgumentException("Endereço de entrega é obrigatório");
        }
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item");
        }
        for (ItemPedido item : itens) {
            if (item.getQuantidade() < 1) {
                throw new IllegalArgumentException(
                    "Quantidade inválida para o produto id=" + item.getItem().getId());
            }
        }

        Cliente cliente = clienteRepository.buscarPorCpf(clienteCpf);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado para CPF: " + clienteCpf);
        }

        // ── Monta a entidade Pedido ────────────────────────────
        // ID 0 = ainda não persistido; o repositório devolve o ID gerado pelo BD.
        // Valor/impostos/desconto são zerados: serão calculados na etapa de aprovação.
        Pedido pedido = new Pedido(
            0L,
            cliente,
            null,           // data_hora_pagamento — ainda sem pagamento
            itens,
            Pedido.Status.NOVO,
            0.0,            // valor — calculado na aprovação
            0.0,            // impostos — calculado na aprovação
            0.0,            // desconto — calculado na aprovação
            0.0,            // valorCobrado — calculado na aprovação
            enderecoEntrega
        );

        return pedidoRepository.criar(pedido);
    }

    // ─────────────────────────────────────────────────────────
    // CANCELAR PEDIDO — mantém a lógica já existente no projeto
    // ─────────────────────────────────────────────────────────

    /**
     * Cancela um pedido nos status NOVO ou APROVADO.
     * Pedidos PAGO ou posteriores não podem ser cancelados.
     */
    public Pedido cancelar(long id, String canceladoPor) {
        Pedido pedido = pedidoRepository.buscarPorId(id);

        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }

        if (pedido.getStatus() != Pedido.Status.NOVO &&
            pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new RuntimeException(
                "Pedido não pode ser cancelado pois está com status: " + pedido.getStatus());
        }

        pedido.setStatus(Pedido.Status.CANCELADO);
        pedido.setCanceladoPor(canceladoPor);
        pedido.setDataHoraCancelamento(LocalDateTime.now());
        pedidoRepository.salvar(pedido);

        return pedido;
    }
}
