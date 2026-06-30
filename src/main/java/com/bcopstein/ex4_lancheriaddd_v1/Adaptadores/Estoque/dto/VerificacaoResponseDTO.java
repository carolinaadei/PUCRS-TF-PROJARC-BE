package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Estoque.dto;

import java.util.List;

public record VerificacaoResponseDTO(boolean disponivel, List<Long> ingredientesIndisponiveis) {}
