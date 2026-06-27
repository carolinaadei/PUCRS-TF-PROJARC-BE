package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueRepository estoqueRepository;

    public EstoqueController(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    @GetMapping
    public List<ItemEstoqueResponse> listar() {
        return estoqueRepository.recuperaTodos().stream()
                .map(i -> new ItemEstoqueResponse(
                        i.getIngrediente().getId(),
                        i.getIngrediente().getDescricao(),
                        i.getQuantidade()))
                .toList();
    }

    @PutMapping("/{ingredienteId}")
    public ResponseEntity<String> atualizar(@PathVariable long ingredienteId,
                                             @RequestBody AtualizarEstoqueRequest request) {
        ItemEstoque item = estoqueRepository.recuperaPorIngredienteId(ingredienteId)
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado no estoque: " + ingredienteId));
        item.setQuantidade(request.quantidade());
        estoqueRepository.atualizar(item);
        return ResponseEntity.ok("Estoque do ingrediente " + ingredienteId + " atualizado para " + request.quantidade());
    }

    public record ItemEstoqueResponse(long ingredienteId, String descricao, int quantidade) {}
    public record AtualizarEstoqueRequest(int quantidade) {}
}
