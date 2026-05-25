package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.CadastrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CadastrarClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CadastrarClienteUC {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastrarClienteUC(ClienteRepository clienteRepository,
                              PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CadastrarClienteResponse run(CadastrarClienteRequest request) {

        if (clienteRepository.existePorEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        if (clienteRepository.existePorCpf(request.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        String senhaHash = passwordEncoder.encode(request.senha());

        Cliente cliente = new Cliente(
                null,
                request.nome(),
                request.cpf(),
                request.celular(),
                request.endereco(),
                request.email(),
                senhaHash
        );

        Cliente salvo = clienteRepository.cadastrar(cliente);

        return new CadastrarClienteResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail()
        );
    }
}