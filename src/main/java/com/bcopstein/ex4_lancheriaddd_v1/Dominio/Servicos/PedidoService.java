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
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CalculadoraPreco.ResultadoCalculo;

/**
 * Serviço de domínio responsável pelas operações sobre Pedido.
 *
 * Regras de negócio implementadas aqui:
 * - O cliente deve existir no cadastro.
 * - O pedido deve conter ao menos um item.
 * - Cada item deve ter quantidade >= 1.
 * - O endereço de entrega não pode ser vazio.
 * - Apenas pedidos com status NOVO ou APROVADO podem ser cancelados.
 *
 * Esta classe SUBSTITUI o PedidoService já existente no projeto,
 * unificando submissão e cancelamento em um único serviço de domínio.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final CalculadoraPreco calculadoraPreco;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            CalculadoraPreco calculadoraPreco) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.calculadoraPreco = calculadoraPreco;
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

        Cliente cliente = clienteRepository.recuperaPorCpf(clienteCpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado para CPF: " + clienteCpf));

        // ── Calcula preço ──────────────────────────────────────
        ResultadoCalculo preco = calculadoraPreco.calcular(itens);

        // ── Monta a entidade Pedido ────────────────────────────
        Pedido pedido = new Pedido(
                0L,
                cliente,
                null,
                itens,
                Pedido.Status.NOVO,
                preco.valor(),
                preco.impostos(),
                preco.desconto(),
                preco.valorCobrado(),
                enderecoEntrega);

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
        Pedido pedido = pedidoRepository.recuperaPorId(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

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

    public List<Pedido> listarEntreguesEntre(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return pedidoRepository.buscarEntreguesEntre(inicio, fim);
    }

    public List<Pedido> listarEntreguesPorClienteEntre(String clienteCpf, LocalDateTime inicio, LocalDateTime fim) {
        if (clienteCpf == null || clienteCpf.isBlank()) {
            throw new IllegalArgumentException("CPF do cliente é obrigatório");
        }
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }
        return pedidoRepository.buscarEntreguesPorClienteEntre(clienteCpf, inicio, fim);
    }
}
