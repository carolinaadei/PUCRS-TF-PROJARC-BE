package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private CancelarPedidoUC cancelarPedidoUC;
    private ListarPedidosEntreguesUC listarPedidosEntreguesUC;
    private ListarPedidosClienteUC listarPedidosClienteUC;

    public PedidoController(CancelarPedidoUC cancelarPedidoUC,
            ListarPedidosEntreguesUC listarPedidosEntreguesUC,
            ListarPedidosClienteUC listarPedidosClienteUC) {
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
        this.listarPedidosClienteUC = listarPedidosClienteUC;
    }

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

    @GetMapping("/entregues/cliente")
    @CrossOrigin("*")
    public List<PedidoResponse> listarEntreguesPorCliente(
            @RequestParam String cpf,
            @RequestParam String inicio,
            @RequestParam String fim) {
        try {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio);
            LocalDateTime dataFim = LocalDateTime.parse(fim);
            return listarPedidosClienteUC.run(cpf, dataInicio, dataFim);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato de data inválido. Use o formato: yyyy-MM-ddTHH:mm:ss");
        }
    }
}