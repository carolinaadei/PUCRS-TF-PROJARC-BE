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

import java.util.List;

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
    public boolean verifyItem(ItemPedido item) {
        Receita receita = item.getItem().getReceita();
        List<PorcaoIngrediente> porcoes = receita == null ? List.of() : receita.getPorcoes();

        if (porcoes.isEmpty()) {
            log.warn("Produto {} sem receita definida — verificação de estoque ignorada", item.getItem().getId());
            return true;
        }

        List<IngredienteQuantidadeDTO> ingredientes = porcoes.stream()
            .map(porcao -> new IngredienteQuantidadeDTO(
                porcao.getIngrediente().getId(),
                porcao.getQuantidade() * item.getQuantidade()))
            .toList();

        VerificacaoRequestDTO request = new VerificacaoRequestDTO(ingredientes);

        try {
            VerificacaoResponseDTO response = restTemplate.postForObject(
                estoqueServiceUrl + "/estoque/verificar",
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
