package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.Entidades.*;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ClienteJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.PedidoJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.ProdutoJpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Pedido salvar(Pedido pedido) {
        ClienteEntity clienteEntity = clienteJpa.findById(pedido.getCliente().getCpf())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + pedido.getCliente().getCpf()));

        PedidoEntity entity = new PedidoEntity();
        entity.setCliente(clienteEntity);
        entity.setDataHoraPagamento(pedido.getDataHoraPagamento());
        entity.setStatus(pedido.getStatus());
        entity.setValor(pedido.getValor());
        entity.setImpostos(pedido.getImpostos());
        entity.setDesconto(pedido.getDesconto());
        entity.setValorCobrado(pedido.getValorCobrado());

        List<ItemPedidoEntity> itens = pedido.getItens().stream().map(item -> {
            ProdutoEntity produtoEntity = produtoJpa.findById(item.getItem().getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + item.getItem().getId()));
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
    public Optional<Pedido> recuperaPorId(long id) {
        return pedidoJpa.findById(id).map(this::toDomain);
    }

    @Override
    public List<Pedido> recuperaTodos() {
        return pedidoJpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public void atualizarStatus(long id, Pedido.Status status) {
        pedidoJpa.findById(id).ifPresent(entity -> {
            entity.setStatus(status);
            pedidoJpa.save(entity);
        });
    }

    private Pedido toDomain(PedidoEntity e) {
        Cliente cliente = new Cliente(
            e.getCliente().getCpf(),
            e.getCliente().getNome(),
            e.getCliente().getCelular(),
            e.getCliente().getEndereco(),
            e.getCliente().getEmail()
        );

        List<ItemPedido> itens = e.getItens().stream().map(item -> {
            ProdutoEntity p = item.getProduto();
            Produto produto = new Produto(p.getId(), p.getDescricao(), null, p.getPreco().intValue());
            return new ItemPedido(produto, item.getQuantidade());
        }).toList();

        return new Pedido(
            e.getId(),
            cliente,
            e.getDataHoraPagamento(),
            itens,
            e.getStatus(),
            e.getValor(),
            e.getImpostos(),
            e.getDesconto(),
            e.getValorCobrado()
        );
    }
}
