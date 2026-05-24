package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AcompanharPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AcompanhamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;

/**
 * Adaptador de apresentação: Controller REST para operações sobre Pedido.
 *
 * Consolida em um único controller os endpoints de pedido,
 * substituindo o PedidoController original (que só tinha /cancelar).
 *
 * Endpoints expostos:
 *   POST /pedidos          → submete um novo pedido (status NOVO)
 *   POST /pedidos/{id}/cancelar → cancela um pedido NOVO ou APROVADO
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final CancelarPedidoUC cancelarPedidoUC;
    private final ListarPedidosEntreguesUC listarPedidosEntreguesUC;
    private final AcompanharPedidoUC acompanharPedidoUC;

    public PedidoController(CancelarPedidoUC cancelarPedidoUC,
                            ListarPedidosEntreguesUC listarPedidosEntreguesUC,
                            AcompanharPedidoUC acompanharPedidoUC,
                            SubmeterPedidoUC submeterPedidoUC) {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final CancelarPedidoUC cancelarPedidoUC;

        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
        this.acompanharPedidoUC = acompanharPedidoUC;
    }

    /**
     * POST /pedidos
     *
     * Payload esperado:
     * {
     *   "clienteCpf": "9001",
     *   "enderecoEntrega": "Rua das Flores, 100",
     *   "itens": [
     *     { "produtoId": 1, "quantidade": 2 },
     *     { "produtoId": 3, "quantidade": 1 }
     *   ]
     * }
     *
     * Resposta 201 Created:
     * {
     *   "idPedido": 3,
     *   "status": "NOVO",
     *   "mensagem": "Pedido submetido com sucesso",
     *   "enderecoEntrega": "Rua das Flores, 100"
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("*")
    public SubmeterPedidoResponse submeterPedido(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoUC.run(request);
    }

    /**
     * POST /pedidos/{id}/cancelar?canceladoPor=cliente
     *
     * Preservado do PedidoController original.
     */
    @PostMapping("/{id}/cancelar")
    @CrossOrigin("*")
    public CancelarPedidoResponse cancelarPedido(
            @PathVariable(value = "id") long id,
            @RequestParam String canceladoPor) {
        return cancelarPedidoUC.run(id, canceladoPor);
    }

    @GetMapping("/entregues")
    @CrossOrigin("*")
    public List<PedidoResponse> listarEntregues(
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio);
            LocalDateTime dataFim = LocalDateTime.parse(fim);
            return listarPedidosEntreguesUC.run(dataInicio, dataFim);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválido. Use o formato: yyyy-MM-ddTHH:mm:ss");
        }
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public AcompanhamentoPedidoResponse acompanharPedido(
            @PathVariable(value = "id") long id,
            @RequestParam String cpf) {
        return acompanharPedidoUC.run(id, cpf);
    }
}