package com.bcopstein.ex4_lancheriaddd_v1.Infra;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoPolicy;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.SemDesconto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class DescontoConfig {

    @Primary
    @Bean
    public DescontoPolicy politicaCorrente(
            List<DescontoPolicy> todasPoliticas,
            @Value("${politica.desconto:" + SemDesconto.CODIGO + "}") String codigo) {
        return todasPoliticas.stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Política de desconto desconhecida: '" + codigo + "'. " +
                "Políticas disponíveis: " + todasPoliticas.stream().map(DescontoPolicy::getCodigo).toList()
            ));
    }
}
