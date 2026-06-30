package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque.dto.IngredienteQuantidadeDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque.dto.VerificacaoRequestDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque.dto.VerificacaoResponseDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.PorcaoIngrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StockHttpAdapter implements IStockService {

    private static final Logger log = LoggerFactory.getLogger(StockHttpAdapter.class);

    private final RestTemplate restTemplate;

    @Value("${estoque.service.url:http://estoque-service:8001}")
    private String estoqueServiceUrl;

    public StockHttpAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public VerificacaoResultado verificarDisponibilidade(List<ItemPedido> itens) {
        Map<Long, Integer> agregados = new HashMap<>();

        for (ItemPedido item : itens) {
            Receita receita = item.getItem().getReceita();
            if (receita == null || receita.getPorcoes() == null) {
                log.warn("Produto {} sem receita — ingredientes ignorados na verificação", item.getItem().getId());
                continue;
            }
            for (PorcaoIngrediente porcao : receita.getPorcoes()) {
                Long id = porcao.getIngrediente().getId();
                int qtd = porcao.getQuantidade() * item.getQuantidade();
                agregados.merge(id, qtd, Integer::sum);
            }
        }

        if (agregados.isEmpty()) {
            log.warn("Nenhum ingrediente encontrado nos itens do pedido — verificação de estoque ignorada");
            return new VerificacaoResultado(true, List.of());
        }

        List<IngredienteQuantidadeDTO> lista = agregados.entrySet().stream()
            .map(e -> new IngredienteQuantidadeDTO(e.getKey(), e.getValue()))
            .toList();

        VerificacaoRequestDTO request = new VerificacaoRequestDTO(lista);

        try {
            VerificacaoResponseDTO response = restTemplate.postForObject(
                estoqueServiceUrl + "/estoque/verificar",
                request,
                VerificacaoResponseDTO.class);

            if (response == null) {
                throw new RuntimeException("Resposta nula do serviço de estoque");
            }

            if (!response.disponivel()) {
                log.warn("Estoque insuficiente. Ingredientes em falta: {}", response.ingredientesIndisponiveis());
            }

            return new VerificacaoResultado(
                response.disponivel(),
                response.ingredientesIndisponiveis() != null ? response.ingredientesIndisponiveis() : List.of());

        } catch (RestClientException e) {
            throw new RuntimeException("Serviço de estoque indisponível: " + e.getMessage(), e);
        }
    }
}
