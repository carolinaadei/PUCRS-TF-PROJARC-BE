package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de saída para o endpoint de acompanhamento de pedido.
 *
 * Formato de resposta:
 * {
 *   "idPedido": 3,
 *   "statusAtual": "PREPARACAO",
 *   "descricaoStatus": "Pedido sendo preparado",
 *   "enderecoEntrega": "Rua das Flores, 100",
 *   "dataHoraPagamento": "2026-05-14T14:30:00",
 *   "historico": [
 *     { "status": "NOVO",       "dataHora": "2026-05-14T14:00:00", "responsavel": "cliente"  },
 *     { "status": "APROVADO",   "dataHora": "2026-05-14T14:05:00", "responsavel": "sistema"  },
 *     { "status": "PAGO",       "dataHora": "2026-05-14T14:30:00", "responsavel": "cliente"  },
 *     { "status": "AGUARDANDO", "dataHora": "2026-05-14T14:31:00", "responsavel": "cozinha"  },
 *     { "status": "PREPARACAO", "dataHora": "2026-05-14T14:45:00", "responsavel": "cozinha"  }
 *   ]
 * }
 */
public class AcompanhamentoPedidoResponse {

    private long idPedido;
    private String statusAtual;
    private String descricaoStatus;
    private String enderecoEntrega;
    private LocalDateTime dataHoraPagamento;
    private List<EntradaHistoricoResponse> historico;

    public AcompanhamentoPedidoResponse(long idPedido, String statusAtual,
                                         String descricaoStatus, String enderecoEntrega,
                                         LocalDateTime dataHoraPagamento,
                                         List<EntradaHistoricoResponse> historico) {
        this.idPedido = idPedido;
        this.statusAtual = statusAtual;
        this.descricaoStatus = descricaoStatus;
        this.enderecoEntrega = enderecoEntrega;
        this.dataHoraPagamento = dataHoraPagamento;
        this.historico = historico;
    }

    public long getIdPedido() { return idPedido; }
    public String getStatusAtual() { return statusAtual; }
    public String getDescricaoStatus() { return descricaoStatus; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
    public LocalDateTime getDataHoraPagamento() { return dataHoraPagamento; }
    public List<EntradaHistoricoResponse> getHistorico() { return historico; }

    // ── Inner record: cada linha do histórico ─────────────────────────────────
    public record EntradaHistoricoResponse(
        String status,
        LocalDateTime dataHora,
        String responsavel
    ) {}
}