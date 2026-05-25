package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public record CadastrarClienteResponse(
        Long id,
        String nome,
        String email
) {
}