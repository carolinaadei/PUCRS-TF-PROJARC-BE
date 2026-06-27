package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Primary
@Service
public class HttpEstoqueService implements IStockService {

    private static final Logger log = LoggerFactory.getLogger(HttpEstoqueService.class);

    private final RestTemplate restTemplate;

    @Value("${estoque.service.url:http://estoque-service:8001}")
    private String estoqueServiceUrl;

    public HttpEstoqueService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean verifyItem(ItemPedido item) {
        Receita receita = item.getItem().getReceita();
        if (receita == null || receita.getIngredientes() == null || receita.getIngredientes().isEmpty()) {
            log.warn("Item {} sem receita definida - assumindo disponível", item.getItem().getId());
            return true;
        }

        List<Map<String, Object>> ingredientes = receita.getIngredientes().stream()
            .map((Ingrediente ing) -> Map.<String, Object>of(
                "ingredienteId", ing.getId(),
                "quantidade",    item.getQuantidade()
            ))
            .toList();

        Map<String, Object> requestBody = Map.of("ingredientes", ingredientes);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(
                estoqueServiceUrl + "/estoque/verificar",
                requestBody,
                Map.class
            );
            boolean disponivel = response != null && Boolean.TRUE.equals(response.get("disponivel"));
            if (!disponivel) {
                log.warn("Estoque insuficiente para o item {}", item.getItem().getId());
            }
            return disponivel;
        } catch (Exception e) {
            log.error("Erro ao chamar estoque-service: {}. Assumindo disponível.", e.getMessage());
            return true;
        }
    }

    @Override
    public void getStock(List<ItemPedido> itens) {
        // Não utilizado no fluxo atual
    }
}
