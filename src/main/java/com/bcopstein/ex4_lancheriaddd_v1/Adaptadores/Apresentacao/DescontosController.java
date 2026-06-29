package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoPolicy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/descontos")
public class DescontosController {

    private final DescontoPolicy politicaCorrente;

    public DescontosController(DescontoPolicy politicaCorrente) {
        this.politicaCorrente = politicaCorrente;
    }

    @GetMapping("/corrente")
    public DescontoPoliticaResponse corrente() {
        return new DescontoPoliticaResponse(politicaCorrente.getCodigo());
    }

    @GetMapping("/verificar")
    public DescontoElegibilidadeResponse verificar(@RequestParam String cpf) {
        boolean elegivel = politicaCorrente.seAplica(cpf);
        return new DescontoElegibilidadeResponse(politicaCorrente.getCodigo(), cpf, elegivel);
    }

    public record DescontoPoliticaResponse(String politica) {}
    public record DescontoElegibilidadeResponse(String politica, String cpf, boolean elegivelParaDesconto) {}
}
