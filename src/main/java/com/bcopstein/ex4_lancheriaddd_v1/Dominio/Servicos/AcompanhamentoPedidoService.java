package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoStatusRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PedidoStatusHistorico;

/**
 * Serviço de domínio para consulta de acompanhamento de pedido.
 *
 * Regras de negócio:
 *  - O pedido deve existir; caso contrário lança exceção com mensagem clara.
 *  - O CPF informado deve corresponder ao cliente dono do pedido
 *    (autorização simples no nível de domínio, sem Spring Security).
 *  - Retorna o status atual e o histórico cronológico de todas as transições.
 */
@Service
public class AcompanhamentoPedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStatusRepository pedidoStatusRepository;

    @Autowired
    private AcompanhamentoPedidoService(PedidoRepository pedidoRepository,
                                        PedidoStatusRepository pedidoStatusRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoStatusRepository = pedidoStatusRepository;
    }

    /**
     * Retorna o pedido e seu histórico de status para o cliente informado.
     *
     * @param pedidoId   Número do pedido gerado na submissão
     * @param clienteCpf CPF do cliente que está consultando
     * @return O pedido com status atual
     * @throws IllegalArgumentException se o pedido não existir
     * @throws AcessoNaoAutorizadoException se o CPF não corresponder ao dono do pedido
     */
    public Pedido consultarStatus(long pedidoId, String clienteCpf) {

        // ── Valida existência ─────────────────────────────────────────────────
        Pedido pedido = pedidoRepository.recuperaPorId(pedidoId).orElse(null);
        if (pedido == null) {
            throw new IllegalArgumentException(
                "Pedido não encontrado para o número: " + pedidoId);
        }

        // ── Valida autorização: CPF deve ser o do dono do pedido ─────────────
        if (pedido.getCliente() == null ||
            !pedido.getCliente().getCpf().equals(clienteCpf)) {
            throw new AcessoNaoAutorizadoException(
                "Acesso não autorizado: o pedido " + pedidoId +
                " não pertence ao CPF informado");
        }

        return pedido;
    }

    /**
     * Retorna o histórico cronológico de mudanças de status do pedido.
     * Não valida autorização — deve ser chamado após {@link #consultarStatus}.
     */
    public List<PedidoStatusHistorico> consultarHistorico(long pedidoId) {
        return pedidoStatusRepository.buscarHistoricoPorPedidoId(pedidoId);
    }
}