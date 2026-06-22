package com.pizzaria.estoque.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record VerificacaoRequest(@NotEmpty List<IngredienteQuantidadeDTO> ingredientes) {}
