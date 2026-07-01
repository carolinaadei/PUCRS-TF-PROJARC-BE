package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CalculadoraPreco.ResultadoCalculo;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IStockService.VerificacaoResultado;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;
    private final CalculadoraPreco calculadoraPreco;
    private final IPaymentService paymentService;
    private final IStockService stockService;
    private final ProdutosRepository produtosRepository;

    public PedidoService(PedidoRepository pedidoRepository,
            PedidoStatusRepository statusRepository,
            CalculadoraPreco calculadoraPreco,
            IPaymentService paymentService,
            IStockService stockService,
            ProdutosRepository produtosRepository) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        this.calculadoraPreco = calculadoraPreco;
        this.paymentService = paymentService;
        this.stockService = stockService;
        this.produtosRepository = produtosRepository;
    }

    @Transactional
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

        VerificacaoResultado resultado = stockService.verificarDisponibilidade(itens);

        if (!resultado.disponivel()) {
            Set<Long> indisponiveis = new HashSet<>(resultado.ingredientesIndisponiveis());
            itens.stream()
                .filter(item -> item.getItem().getReceita() != null &&
                    item.getItem().getReceita().getPorcoes().stream()
                        .anyMatch(p -> indisponiveis.contains(p.getIngrediente().getId())))
                .forEach(item -> produtosRepository.atualizarDisponibilidade(item.getItem().getId(), false));

            throw new IllegalStateException(
                    "Estoque insuficiente para os itens: " + itens.stream()
                            .filter(item -> item.getItem().getReceita() != null &&
                                item.getItem().getReceita().getPorcoes().stream()
                                    .anyMatch(p -> indisponiveis.contains(p.getIngrediente().getId())))
                            .map(i -> String.valueOf(i.getItem().getId()))
                            .toList());
        }

        ResultadoCalculo preco = calculadoraPreco.calcular(itens, clienteCpf);
        Cliente cliente = new Cliente(null, null, clienteCpf, null, null, null, null);

        Pedido pedido = new Pedido(
                0L,
                cliente,
                null,
                itens,
                Pedido.Status.APROVADO,
                preco.valor(),
                preco.impostos(),
                preco.desconto(),
                preco.valorCobrado(),
                enderecoEntrega);

        Pedido criado = pedidoRepository.criar(pedido);

        statusRepository.registrar(new PedidoStatusHistorico(
                criado.getId(), Pedido.Status.NOVO, LocalDateTime.now(), "cliente"));
        statusRepository.registrar(new PedidoStatusHistorico(
                criado.getId(), Pedido.Status.APROVADO, LocalDateTime.now(), "sistema"));

        return criado;
    }

    @Transactional
    public Pedido cancelar(long id, String canceladoPor) {
        Pedido pedido = pedidoRepository.recuperaPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado"));

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalArgumentException(
                    "Pedido não pode ser cancelado pois está com status: " + pedido.getStatus());
        }

        pedido.setStatus(Pedido.Status.CANCELADO);
        pedido.setCanceladoPor(canceladoPor);
        pedido.setDataHoraCancelamento(LocalDateTime.now());
        pedidoRepository.salvar(pedido);

        statusRepository.registrar(new PedidoStatusHistorico(
                pedido.getId(), Pedido.Status.CANCELADO, LocalDateTime.now(), canceladoPor));

        return pedido;
    }

    @Transactional
    public Pedido pagar(long id) {
        Pedido pedido = pedidoRepository.recuperaPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado"));

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalArgumentException(
                    "Pedido não pode ser pago pois está com status: " + pedido.getStatus());
        }

        boolean pagamentoAprovado = paymentService.processPayment(pedido.getId(), pedido.getValorCobrado());

        if (!pagamentoAprovado) {
            throw new RuntimeException("Pagamento recusado");
        }

        pedido.setStatus(Pedido.Status.PAGO);
        pedido.setDataHoraPagamento(LocalDateTime.now());
        pedidoRepository.salvar(pedido);

        statusRepository.registrar(new PedidoStatusHistorico(
                pedido.getId(), Pedido.Status.PAGO, LocalDateTime.now(), "cliente"));

        return pedidoRepository.recuperaPorId(pedido.getId()).orElse(pedido);
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

    @Transactional
    public Pedido confirmarEntrega(long id) {
        Pedido pedido = pedidoRepository.recuperaPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado"));

        if (pedido.getStatus() != Pedido.Status.TRANSPORTE) {
            throw new IllegalArgumentException(
                    "Pedido não pode ser confirmado como entregue pois está com status: " + pedido.getStatus());
        }

        pedido.setStatus(Pedido.Status.ENTREGUE);
        pedidoRepository.salvar(pedido);
        statusRepository.registrar(new PedidoStatusHistorico(
                pedido.getId(), Pedido.Status.ENTREGUE, LocalDateTime.now(), "delivery-service"));

        return pedido;
    }

    public List<Pedido> listarPorCliente(String clienteCpf) {
        if (clienteCpf == null || clienteCpf.isBlank()) {
            throw new IllegalArgumentException("CPF do cliente é obrigatório");
        }
        return pedidoRepository.buscarPorCliente(clienteCpf);
    }
}
