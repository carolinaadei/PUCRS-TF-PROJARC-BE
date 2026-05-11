package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EstoqueService implements IEstoqueService {

    private final EstoqueRepository estoqueRepository;

    @Override
    @Transactional
    public ResultadoVerificacao verificarEDescontar(List<ItemPedido> itens) {
        // Agrega a necessidade total de cada ingrediente
        Map<Long, BigDecimal> necessidade = new HashMap<>();
        for (ItemPedido ip : itens) {
            for (Receita r : ip.getItemCardapio().getReceitas()) {
                Long ingId = r.getIngrediente().getId();
                BigDecimal qtdNecessaria = r.getQuantidade()
                        .multiply(BigDecimal.valueOf(ip.getQuantidade()));
                necessidade.merge(ingId, qtdNecessaria, BigDecimal::add);
            }
        }

        // Verifica disponibilidade
        List<Long> itensSemEstoque = new ArrayList<>();
        Map<Long, ItemEstoque> estoqueMap = new HashMap<>();

        for (Map.Entry<Long, BigDecimal> entry : necessidade.entrySet()) {
            Long ingId = entry.getKey();
            BigDecimal qtdNecessaria = entry.getValue();
            Optional<ItemEstoque> opt = estoqueRepository.findByIngredienteId(ingId);
            if (opt.isEmpty() || !opt.get().temSuficiente(qtdNecessaria)) {
                itensSemEstoque.add(ingId);
            } else {
                estoqueMap.put(ingId, opt.get());
            }
        }

        if (!itensSemEstoque.isEmpty()) {
            return new ResultadoVerificacao(false, itensSemEstoque);
        }

        // Desconta do estoque
        estoqueMap.forEach((ingId, estoque) -> {
            estoque.consumir(necessidade.get(ingId));
            estoqueRepository.save(estoque);
        });

        return new ResultadoVerificacao(true, List.of());
    }
}
