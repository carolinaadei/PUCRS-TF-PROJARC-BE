package com.pizzaria.estoque.controller;

import com.pizzaria.estoque.dto.*;
import com.pizzaria.estoque.service.EstoqueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public List<ItemEstoqueResponse> listar() {
        return estoqueService.listarTodos();
    }

    @PostMapping("/verificar")
    public ResponseEntity<VerificacaoResponse> verificar(@Valid @RequestBody VerificacaoRequest request) {
        return ResponseEntity.ok(estoqueService.verificarEConsumir(request));
    }

    @PutMapping("/{ingredienteId}")
    public ResponseEntity<String> atualizar(
            @PathVariable Long ingredienteId,
            @RequestBody AtualizarQuantidadeRequest request) {
        estoqueService.atualizarQuantidade(ingredienteId, request.quantidade());
        return ResponseEntity.ok("Estoque atualizado: ingrediente " + ingredienteId + " = " + request.quantidade());
    }

    public record AtualizarQuantidadeRequest(Integer quantidade) {}
}
