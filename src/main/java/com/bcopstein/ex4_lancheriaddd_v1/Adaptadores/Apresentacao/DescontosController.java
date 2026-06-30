package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ConfiguracaoDesconto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoPolicy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/descontos")
public class DescontosController {

    private final ConfiguracaoDesconto configuracaoDesconto;

    public DescontosController(ConfiguracaoDesconto configuracaoDesconto) {
        this.configuracaoDesconto = configuracaoDesconto;
    }

    @GetMapping("/corrente")
    public DescontoPoliticaResponse corrente() {
        return new DescontoPoliticaResponse(configuracaoDesconto.getPoliticaCorrente().getCodigo());
    }

    @GetMapping("/verificar")
    public DescontoElegibilidadeResponse verificar(@RequestParam String cpf) {
        DescontoPolicy politica = configuracaoDesconto.getPoliticaCorrente();
        boolean elegivel = politica.seAplica(cpf);
        return new DescontoElegibilidadeResponse(politica.getCodigo(), cpf, elegivel);
    }

    public record DescontoPoliticaResponse(String politica) {}
    public record DescontoElegibilidadeResponse(String politica, String cpf, boolean elegivelParaDesconto) {}
}
