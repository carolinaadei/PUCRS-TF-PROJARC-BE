package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.*;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ClienteJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.PedidoJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ProdutoJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PedidoRepositoryJPA implements PedidoRepository {

    private final PedidoJpaRepository pedidoJpa;
    private final ClienteJpaRepository clienteJpa;
    private final ProdutoJpaRepository produtoJpa;

    @Autowired
    public PedidoRepositoryJPA(PedidoJpaRepository pedidoJpa,
            ClienteJpaRepository clienteJpa,
            ProdutoJpaRepository produtoJpa) {
        this.pedidoJpa = pedidoJpa;
        this.clienteJpa = clienteJpa;
        this.produtoJpa = produtoJpa;
    }

    @Override
    @Transactional
    public Pedido criar(Pedido pedido) {
        ClienteEntity clienteEntity = clienteJpa.findById(pedido.getCliente().getCpf())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente não encontrado: " + pedido.getCliente().getCpf()));

        PedidoEntity entity = new PedidoEntity();
        entity.setCliente(clienteEntity);
        entity.setStatus(pedido.getStatus());
        entity.setValor(pedido.getValor());
        entity.setImpostos(pedido.getImpostos());
        entity.setDesconto(pedido.getDesconto());
        entity.setValorCobrado(pedido.getValorCobrado());
        entity.setEnderecoEntrega(pedido.getEnderecoEntrega());
        entity.setDataHoraPagamento(pedido.getDataHoraPagamento());

        List<ItemPedidoEntity> itens = pedido.getItens().stream().map(item -> {
            ProdutoEntity produtoEntity = produtoJpa.findById(item.getItem().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Produto não encontrado: " + item.getItem().getId()));
            ItemPedidoEntity itemEntity = new ItemPedidoEntity();
            itemEntity.setPedido(entity);
            itemEntity.setProduto(produtoEntity);
            itemEntity.setQuantidade(item.getQuantidade());
            return itemEntity;
        }).toList();

        entity.setItens(itens);
        return toDomain(pedidoJpa.save(entity));
    }

    @Override
    public Pedido buscarPorId(long id) {
        return pedidoJpa.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    @Transactional
    public void salvar(Pedido pedido) {
        PedidoEntity entity = pedidoJpa.findById(pedido.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pedido não encontrado: " + pedido.getId()));
        entity.setStatus(pedido.getStatus());
        entity.setCanceladoPor(pedido.getCanceladoPor());
        entity.setDataHoraCancelamento(pedido.getDataHoraCancelamento());
        entity.setDataHoraPagamento(pedido.getDataHoraPagamento());
        pedidoJpa.save(entity);
    }

    @Override
    public List<Pedido> buscarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoJpa.findEntreguesByDataHora(Pedido.Status.ENTREGUE, inicio, fim)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<Pedido> buscarEntreguesPorClienteEntre(
            String clienteCpf, LocalDateTime inicio, LocalDateTime fim) {
        return pedidoJpa.findEntreguesByClienteAndDataHora(
                Pedido.Status.ENTREGUE, clienteCpf, inicio, fim)
                .stream().map(this::toDomain).toList();
    }

    private Pedido toDomain(PedidoEntity e) {
        Cliente cliente = e.getCliente() != null
                ? new Cliente(e.getCliente().getCpf(), e.getCliente().getNome(),
                        e.getCliente().getCelular(), e.getCliente().getEndereco(),
                        e.getCliente().getEmail())
                : null;
        Pedido pedido = new Pedido(
                e.getId(),
                cliente,
                e.getDataHoraPagamento(),
                List.of(),
                e.getStatus(),
                e.getValor() != null ? e.getValor() : 0.0,
                e.getImpostos() != null ? e.getImpostos() : 0.0,
                e.getDesconto() != null ? e.getDesconto() : 0.0,
                e.getValorCobrado() != null ? e.getValorCobrado() : 0.0,
                e.getEnderecoEntrega());
        pedido.setCanceladoPor(e.getCanceladoPor());
        pedido.setDataHoraCancelamento(e.getDataHoraCancelamento());
        return pedido;
    }
}
