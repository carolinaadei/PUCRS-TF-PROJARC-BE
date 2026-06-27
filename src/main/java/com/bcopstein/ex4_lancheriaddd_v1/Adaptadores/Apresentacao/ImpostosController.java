package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IImpostoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/impostos")
public class ImpostosController {

    private final IImpostoService impostoService;

    public ImpostosController(IImpostoService impostoService) {
        this.impostoService = impostoService;
    }

    @GetMapping("/calcular")
    public ImpostoResponse calcular(@RequestParam double valorBase) {
        double imposto = impostoService.calcular(valorBase);
        return new ImpostoResponse(valorBase, imposto, valorBase + imposto);
    }

    public record ImpostoResponse(double valorBase, double imposto, double valorComImposto) {}
}
