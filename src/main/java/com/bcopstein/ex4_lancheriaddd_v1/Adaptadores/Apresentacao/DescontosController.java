package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoPolicy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descontos")
public class DescontosController {

    private final List<DescontoPolicy> policies;

    public DescontosController(List<DescontoPolicy> policies) {
        this.policies = policies;
    }

    @GetMapping("/verificar")
    public DescontoElegibilidadeResponse verificar(@RequestParam String cpf) {
        boolean elegivel = policies.stream().anyMatch(p -> p.seAplica(cpf));
        return new DescontoElegibilidadeResponse(cpf, elegivel);
    }

    public record DescontoElegibilidadeResponse(String cpf, boolean elegivelParaDesconto) {}
}
