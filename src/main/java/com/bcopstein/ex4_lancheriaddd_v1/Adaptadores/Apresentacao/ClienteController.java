package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CadastrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.CadastrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CadastrarClienteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CadastrarClienteUC cadastrarClienteUC;

    public ClienteController(CadastrarClienteUC cadastrarClienteUC) {
        this.cadastrarClienteUC = cadastrarClienteUC;
    }

    @PostMapping
    public ResponseEntity<CadastrarClienteResponse> cadastrar(
            @Valid @RequestBody CadastrarClienteRequest request
    ) {

        CadastrarClienteResponse response =
                cadastrarClienteUC.run(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}