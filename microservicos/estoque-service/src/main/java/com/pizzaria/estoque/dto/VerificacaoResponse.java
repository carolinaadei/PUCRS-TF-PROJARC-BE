package com.pizzaria.estoque.dto;

import java.util.List;

public record VerificacaoResponse(boolean disponivel, List<Long> ingredientesIndisponiveis) {}
