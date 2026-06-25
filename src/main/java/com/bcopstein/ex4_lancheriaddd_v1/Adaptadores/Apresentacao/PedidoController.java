package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
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
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ConfirmarEntregaUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AcompanhamentoPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final CancelarPedidoUC cancelarPedidoUC;
    private final ListarPedidosEntreguesUC listarPedidosEntreguesUC;
    private final ListarPedidosClienteUC listarPedidosClienteUC;
    private final PagarPedidoUC pagarPedidoUC;
    private final AcompanharPedidoUC acompanharPedidoUC;
    private final ConfirmarEntregaUC confirmarEntregaUC;

    public PedidoController(CancelarPedidoUC cancelarPedidoUC,
                            SubmeterPedidoUC submeterPedidoUC,
                            ListarPedidosEntreguesUC listarPedidosEntreguesUC,
                            ListarPedidosClienteUC listarPedidosClienteUC,
                            PagarPedidoUC pagarPedidoUC,
                            AcompanharPedidoUC acompanharPedidoUC,
                            ConfirmarEntregaUC confirmarEntregaUC) {
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.submeterPedidoUC = submeterPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
        this.listarPedidosClienteUC = listarPedidosClienteUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.acompanharPedidoUC = acompanharPedidoUC;
        this.confirmarEntregaUC = confirmarEntregaUC;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("*")
    public SubmeterPedidoResponse submeterPedido(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoUC.run(request);
    }

    @PostMapping("/{id}/cancelar")
    @CrossOrigin("*")
    public CancelarPedidoResponse cancelarPedido(
            @PathVariable(value = "id") long id,
            @RequestParam String canceladoPor) {
        return cancelarPedidoUC.run(id, canceladoPor);
    }

    @PostMapping("/{id}/pagar")
    @CrossOrigin("*")
    public PagarPedidoResponse pagarPedido(@PathVariable(value = "id") long id) {
        return pagarPedidoUC.run(id);
    }

    @PostMapping("/{id}/entregue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CrossOrigin("*")
    public void confirmarEntrega(@PathVariable(value = "id") long id) {
        confirmarEntregaUC.run(id);
    }

    @GetMapping("/entregues")
    @CrossOrigin("*")
    public List<PedidoResponse> listarEntregues(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return listarPedidosEntreguesUC.run(inicio, fim);
    }

    @GetMapping("/entregues/cliente")
    @CrossOrigin("*")
    public List<PedidoResponse> listarEntreguesPorCliente(
            @RequestParam String cpf,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return listarPedidosClienteUC.run(cpf, inicio, fim);
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public AcompanhamentoPedidoResponse acompanharPedido(
            @PathVariable(value = "id") long id,
            @RequestParam String cpf) {
        return acompanharPedidoUC.run(id, cpf);
    }
}
