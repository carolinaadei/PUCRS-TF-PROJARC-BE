package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque.dto.IngredienteQuantidadeDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque.dto.VerificacaoRequestDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque.dto.VerificacaoResponseDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Primary
@Service
public class StockHttpAdapter implements IStockService {

    private static final Logger log = LoggerFactory.getLogger(StockHttpAdapter.class);

    private final RestTemplate restTemplate;

    @Value("${stock.service.url:http://stock-service:8001}")
    private String stockServiceUrl;

    public StockHttpAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean verifyItem(ItemPedido item) {
        Receita receita = item.getItem().getReceita();

        if (receita == null || receita.getIngredientes() == null || receita.getIngredientes().isEmpty()) {
            log.warn("Produto {} sem receita definida — verificação de estoque ignorada", item.getItem().getId());
            return true;
        }

        List<IngredienteQuantidadeDTO> ingredientes = receita.getIngredientes().stream()
            .map(ing -> new IngredienteQuantidadeDTO(ing.getId(), item.getQuantidade()))
            .toList();

        VerificacaoRequestDTO request = new VerificacaoRequestDTO(ingredientes);

        try {
            VerificacaoResponseDTO response = restTemplate.postForObject(
                stockServiceUrl + "/estoque/verificar",
                request,
                VerificacaoResponseDTO.class
            );

            if (response == null) {
                throw new RuntimeException("Resposta nula do serviço de estoque");
            }

            if (!response.disponivel()) {
                log.warn("Estoque insuficiente para produto {}. Ingredientes em falta: {}",
                    item.getItem().getId(), response.ingredientesIndisponiveis());
            }

            return response.disponivel();

        } catch (RestClientException e) {
            throw new RuntimeException("Serviço de estoque indisponível: " + e.getMessage(), e);
        }
    }

    @Override
    public void getStock(List<ItemPedido> itens) {
        // não utilizado no fluxo atual de pedidos
    }
}
