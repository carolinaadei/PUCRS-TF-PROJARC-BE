package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido cancelar(long id, String canceladoPor) {
        Pedido pedido = pedidoRepository.buscarPorId(id);

        if (pedido == null) {
            throw new RuntimeException("Pedido não encontrado");
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new RuntimeException("Pedido não pode ser cancelado pois está com status: " + pedido.getStatus());
        }

        pedido.setStatus(Pedido.Status.CANCELADO);
        pedido.setCanceladoPor(canceladoPor);
        pedido.setDataHoraCancelamento(LocalDateTime.now());
        pedidoRepository.salvar(pedido);

        return pedido;
    }

    public List<Pedido> listarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new RuntimeException("Datas de início e fim são obrigatórias");
        }
        if (inicio.isAfter(fim)) {
            throw new RuntimeException("Data de início não pode ser posterior à data de fim");
        }
        return pedidoRepository.buscarEntreguesEntre(inicio, fim);
    }

    public List<Pedido> listarEntreguesPorClienteEntre(String clienteCpf, LocalDateTime inicio, LocalDateTime fim) {
        if (clienteCpf == null || clienteCpf.isBlank()) {
            throw new RuntimeException("CPF do cliente é obrigatório");
        }
        if (inicio == null || fim == null) {
            throw new RuntimeException("Datas de início e fim são obrigatórias");
        }
        if (inicio.isAfter(fim)) {
            throw new RuntimeException("Data de início não pode ser posterior à data de fim");
        }
        return pedidoRepository.buscarEntreguesPorClienteEntre(clienteCpf, inicio, fim);
    }
}