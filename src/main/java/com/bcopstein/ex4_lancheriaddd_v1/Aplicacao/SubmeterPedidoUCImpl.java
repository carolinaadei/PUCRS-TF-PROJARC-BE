package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.UseCases.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.*;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IDescontoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IEstoqueService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IImpostoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UC4 - Submeter pedido para aprovação.
 *
 * Fluxo:
 * 1. Monta o pedido com status NOVO
 * 2. Verifica estoque para todos os itens
 * 3. Se falta estoque → marca itens do cardápio como indisponíveis e retorna negado
 * 4. Se ok → calcula desconto, imposto e custo final → status APROVADO
 */
@Service
@RequiredArgsConstructor
public class SubmeterPedidoUCImpl implements SubmeterPedidoUC {

    private static final int DIAS_FIDELIDADE = 20;

    private final ClienteRepository clienteRepository;
    private final CardapioRepository cardapioRepository;
    private final PedidoRepository pedidoRepository;
    private final IEstoqueService estoqueService;
    private final IDescontoService descontoService;
    private final IImpostoService impostoService;

    @Override
    @Transactional
    public PedidoResponse executar(Long clienteId, SubmeterPedidoRequest request) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));

        // Monta o pedido
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .status(StatusPedido.NOVO)
                .enderecoEntrega(request.enderecoEntrega())
                .itens(new ArrayList<>())
                .build();

        // Resolve os itens do cardápio e monta os ItemPedido
        Cardapio cardapio = cardapioRepository.findCardapioCorrente()
                .orElseThrow(() -> new IllegalStateException("Nenhum cardápio corrente configurado."));

        for (SubmeterPedidoRequest.ItemPedidoRequest itemReq : request.itens()) {
            ItemCardapio itemCardapio = cardapio.getItens().stream()
                    .filter(ic -> ic.getId().equals(itemReq.itemCardapioId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Item de cardápio não encontrado: " + itemReq.itemCardapioId()));

            ItemPedido ip = ItemPedido.builder()
                    .pedido(pedido)
                    .itemCardapio(itemCardapio)
                    .quantidade(itemReq.quantidade())
                    .precoUnit(itemCardapio.getPrecoUnit())
                    .build();
            pedido.getItens().add(ip);
        }

        // Verifica estoque
        IEstoqueService.ResultadoVerificacao resultado =
                estoqueService.verificarEDescontar(pedido.getItens());

        if (!resultado.aprovado()) {
            // Marca itens do cardápio como indisponíveis
            marcarItensIndisponiveis(cardapio, resultado.itensSemEstoque());
            // Salva pedido como NOVO (negado — não chega a APROVADO)
            Pedido salvo = pedidoRepository.save(pedido);
            return PedidoResponse.from(salvo, resultado.itensSemEstoque());
        }

        // Calcula valores
        BigDecimal custoItens = pedido.getItens().stream()
                .map(ItemPedido::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime vinteDiasAtras = LocalDateTime.now().minusDays(DIAS_FIDELIDADE);
        int pedidosRecentes = pedidoRepository.countPedidosClienteDesde(clienteId, vinteDiasAtras);

        BigDecimal desconto = descontoService.calcular(custoItens, pedidosRecentes);
        BigDecimal valorLiquido = custoItens.subtract(desconto);
        BigDecimal imposto = impostoService.calcular(valorLiquido);
        BigDecimal custoFinal = valorLiquido.add(imposto);

        pedido.setStatus(StatusPedido.APROVADO);
        pedido.setCustoItens(custoItens);
        pedido.setDesconto(desconto);
        pedido.setImposto(imposto);
        pedido.setCustoFinal(custoFinal);

        Pedido salvo = pedidoRepository.save(pedido);
        return PedidoResponse.from(salvo);
    }

    private void marcarItensIndisponiveis(Cardapio cardapio, List<Long> ingredientesSemEstoque) {
        // Marca como indisponível qualquer item do cardápio que use algum ingrediente em falta
        cardapio.getItens().forEach(ic -> {
            boolean semIngrediente = ic.getReceitas().stream()
                    .anyMatch(r -> ingredientesSemEstoque.contains(r.getIngrediente().getId()));
            if (semIngrediente) {
                ic.setDisponivel(false);
            }
        });
    }
}
