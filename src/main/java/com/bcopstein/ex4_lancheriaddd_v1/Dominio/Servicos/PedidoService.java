package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CalculadoraPreco.ResultadoCalculo;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository statusRepository;
    private final CalculadoraPreco calculadoraPreco;
    private final IPaymentService paymentService;
    private final ICozinhaService cozinhaService;
    private final IStockService stockService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
            PedidoStatusRepository statusRepository,
            CalculadoraPreco calculadoraPreco,
            IPaymentService paymentService,
            ICozinhaService cozinhaService,
            IStockService stockService) {
        this.pedidoRepository = pedidoRepository;
        this.statusRepository = statusRepository;
        this.calculadoraPreco = calculadoraPreco;
        this.paymentService = paymentService;
        this.cozinhaService = cozinhaService;
        this.stockService = stockService;
    }

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

        // Verificação de estoque — pedido inicia como NOVO
        List<ItemPedido> itensSemEstoque = itens.stream()
                .filter(item -> !stockService.verifyItem(item))
                .toList();

        if (!itensSemEstoque.isEmpty()) {
            throw new IllegalStateException(
                    "Estoque insuficiente para os itens: " + itensSemEstoque.stream()
                            .map(i -> String.valueOf(i.getItem().getId()))
                            .toList());
        }

        // Estoque suficiente — calcula preço e aprova
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

        Pedido pedidoSalvo = pedidoRepository.recuperaPorId(pedido.getId()).orElse(pedido);
        cozinhaService.chegadaDePedido(pedidoSalvo);

        return pedidoSalvo;
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
