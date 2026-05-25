package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest.ItemPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

/**
 * Use Case: Submeter Pedido.
 *
 * Responsabilidades desta camada:
 *  1. Converter o Request (DTO de entrada) em objetos de domínio.
 *  2. Delegar a lógica de negócio ao PedidoService (domínio).
 *  3. Converter o resultado do domínio em Response (DTO de saída).
 *
 * Não contém regras de negócio — apenas orquestra o fluxo.
 */
@Component
public class SubmeterPedidoUC {

    private final PedidoService pedidoService;
    private final ProdutosRepository produtosRepository;

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService,
                             ProdutosRepository produtosRepository) {
        this.pedidoService = pedidoService;
        this.produtosRepository = produtosRepository;
    }

    public SubmeterPedidoResponse run(SubmeterPedidoRequest request) {

        // ── 1. Validação mínima de campos obrigatórios do payload ────────────
        if (request == null) {
            throw new IllegalArgumentException("Requisição não pode ser nula");
        }
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item");
        }

        // ── 2. Mapear IDs/quantidades → entidades de domínio ────────────────
        List<ItemPedido> itensDominio = request.getItens().stream()
            .map(this::toItemPedido)
            .toList();

        // ── 3. Delegar ao serviço de domínio ────────────────────────────────
        Pedido pedido = pedidoService.submeter(
            request.getClienteCpf(),
            request.getEnderecoEntrega(),
            itensDominio
        );

        // ── 4. Montar e retornar o Response ──────────────────────────────────
        return new SubmeterPedidoResponse(
            pedido.getId(),
            pedido.getStatus().name(),
            "Pedido submetido com sucesso",
            pedido.getEnderecoEntrega()
        );
    }

    // ── Conversão auxiliar ────────────────────────────────────────────────────
    private ItemPedido toItemPedido(ItemPedidoRequest itemReq) {
        if (itemReq.getProdutoId() == null) {
            throw new IllegalArgumentException("produtoId não pode ser nulo");
        }
        Produto produto = produtosRepository.recuperaProdutoPorid(itemReq.getProdutoId());
        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto não encontrado para id=" + itemReq.getProdutoId());
        }
        return new ItemPedido(produto, itemReq.getQuantidade());
    }
}