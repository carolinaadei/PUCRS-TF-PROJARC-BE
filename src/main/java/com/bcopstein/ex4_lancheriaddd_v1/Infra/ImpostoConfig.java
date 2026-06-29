package com.bcopstein.ex4_lancheriaddd_v1.Infra;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IImpostoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoLucroPresumido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoSimples;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ImpostoConfig {

    private final List<IImpostoService> strategies = List.of(
        new ImpostoSimples(),
        new ImpostoLucroPresumido()
    );

    @Bean
    public IImpostoService impostoService(@Value("${tax.strategy:" + ImpostoSimples.ID + "}") String strategyId) {
        return strategies.stream()
            .filter(s -> s.getLei().equals(strategyId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Estratégia de imposto desconhecida: '" + strategyId + "'. " +
                "Estratégias disponíveis: " + strategies.stream().map(IImpostoService::getLei).toList()
            ));
    }
}
