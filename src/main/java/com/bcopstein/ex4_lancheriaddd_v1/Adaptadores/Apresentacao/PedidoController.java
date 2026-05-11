package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.UseCases.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Security.ClienteUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * UC4 - POST /api/pedidos
 * Requer autenticação JWT.
 */
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoResponse> submeterPedido(
            @AuthenticationPrincipal ClienteUserDetails userDetails,
            @Valid @RequestBody SubmeterPedidoRequest request) {

        PedidoResponse response = submeterPedidoUC.executar(userDetails.getClienteId(), request);
        return ResponseEntity.ok(response);
    }
}
