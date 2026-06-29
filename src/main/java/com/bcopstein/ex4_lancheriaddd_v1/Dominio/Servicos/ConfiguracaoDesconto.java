package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracaoDesconto {

    private DescontoPolicy politicaCorrente;
    private final List<DescontoPolicy> todasPoliticas;

    @Autowired
    public ConfiguracaoDesconto(
            List<DescontoPolicy> todasPoliticas,
            @Value("${politica.desconto:SemDesconto}") String codigoInicial) {
        this.todasPoliticas = List.copyOf(todasPoliticas);
        this.politicaCorrente = encontrarPorCodigo(codigoInicial);
    }

    public DescontoPolicy getPoliticaCorrente() {
        return politicaCorrente;
    }

    public List<DescontoPolicy> getTodasPoliticas() {
        return todasPoliticas;
    }

    public void trocarPolitica(String codigo) {
        this.politicaCorrente = encontrarPorCodigo(codigo);
    }

    private DescontoPolicy encontrarPorCodigo(String codigo) {
        return todasPoliticas.stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "Política de desconto desconhecida: '" + codigo + "'. " +
                "Disponíveis: " + todasPoliticas.stream().map(DescontoPolicy::getCodigo).toList()
            ));
    }
}
