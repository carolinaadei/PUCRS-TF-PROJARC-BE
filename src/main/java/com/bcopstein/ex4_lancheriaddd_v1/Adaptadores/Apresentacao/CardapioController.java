package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.UseCases.CarregarCardapioUC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * UC3 - GET /api/cardapio
 * Requer autenticação JWT (header Authorization: Bearer <token>).
 */
@RestController
@RequestMapping("/api/cardapio")
@RequiredArgsConstructor
public class CardapioController {

    private final CarregarCardapioUC carregarCardapioUC;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CardapioResponse> getCardapioCorrente() {
        return ResponseEntity.ok(carregarCardapioUC.executar());
    }
}
