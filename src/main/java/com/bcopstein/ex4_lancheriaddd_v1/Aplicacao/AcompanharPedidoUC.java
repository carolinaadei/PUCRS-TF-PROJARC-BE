package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AcompanhamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.AcompanhamentoPedidoService;

/**
 * Use Case: Acompanhar Pedido.
 *
 * Responsabilidades:
 *  1. Validar parâmetros de entrada básicos.
 *  2. Delegar consulta e validação de autorização ao serviço de domínio.
 *  3. Montar o Response com status atual + histórico cronológico.
 */
@Component
public class AcompanharPedidoUC {

    private final AcompanhamentoPedidoService acompanhamentoService;

    @Autowired
    public AcompanharPedidoUC(AcompanhamentoPedidoService acompanhamentoService) {
        this.acompanhamentoService = acompanhamentoService;
    }

    /**
     * @param pedidoId   Número do pedido informado pelo cliente
     * @param clienteCpf CPF do cliente para validação de acesso
     * @return Response com status atual, histórico e metadados do pedido
     */
    public AcompanhamentoPedidoResponse run(long pedidoId, String clienteCpf) {

        // ── Validação básica de entrada ───────────────────────────────────────
        if (clienteCpf == null || clienteCpf.isBlank()) {
            throw new IllegalArgumentException("CPF do cliente é obrigatório para consulta");
        }

        // ── Consulta com validação de autorização ─────────────────────────────
        Pedido pedido = acompanhamentoService.consultarStatus(pedidoId, clienteCpf);

        // ── Histórico de transições ───────────────────────────────────────────
        List<PedidoStatusHistorico> historico =
            acompanhamentoService.consultarHistorico(pedidoId);

        // ── Monta response ────────────────────────────────────────────────────
        List<AcompanhamentoPedidoResponse.EntradaHistoricoResponse> entradasHistorico =
            historico.stream()
                .map(h -> new AcompanhamentoPedidoResponse.EntradaHistoricoResponse(
                    h.getStatus().name(),
                    h.getDataHora(),
                    h.getResponsavel()
                ))
                .toList();

        return new AcompanhamentoPedidoResponse(
            pedido.getId(),
            pedido.getStatus().name(),
            descricaoStatus(pedido.getStatus()),
            pedido.getCliente().getEndereco(),
            pedido.getDataHoraPagamento(),
            entradasHistorico
        );
    }

    // ── Descrição amigável de cada status para o cliente ─────────────────────
    private String descricaoStatus(Pedido.Status status) {
        return switch (status) {
            case NOVO       -> "Pedido recebido e aguardando aprovação";
            case APROVADO   -> "Pedido aprovado e aguardando pagamento";
            case PAGO       -> "Pagamento confirmado — pedido enviado à cozinha";
            case AGUARDANDO -> "Pedido na fila da cozinha";
            case PREPARACAO -> "Pedido sendo preparado";
            case PRONTO     -> "Pedido pronto — aguardando entregador";
            case TRANSPORTE -> "Pedido a caminho do endereço de entrega";
            case ENTREGUE   -> "Pedido entregue com sucesso";
            case CANCELADO  -> "Pedido cancelado";
        };
    }
}