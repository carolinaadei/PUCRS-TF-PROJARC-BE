package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Security.ClienteUserDetails;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Security.JwtService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * UC1 - POST /api/auth/registrar
 * UC2 - POST /api/auth/login
 *
 * Ambos são públicos (não requerem token).
 * UC1 e UC2 estão previstos para entrega 18/05 mas a infraestrutura
 * já está disponível aqui para não bloquear UC3 e UC4.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // ── Records de request/response ──────────────────────────────────────────

    public record RegistrarRequest(
            @NotBlank String nome,
            @NotBlank String cpf,
            String celular,
            String endereco,
            @NotBlank @Email String email,
            @NotBlank String senha
    ) {}

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String senha
    ) {}

    public record TokenResponse(String token, String email, Long clienteId) {}

    // ── UC1 ──────────────────────────────────────────────────────────────────

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistrarRequest req) {
        if (clienteRepository.existsByEmail(req.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("E-mail já cadastrado.");
        }
        if (clienteRepository.existsByCpf(req.cpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("CPF já cadastrado.");
        }

        Cliente cliente = Cliente.builder()
                .nome(req.nome())
                .cpf(req.cpf())
                .celular(req.celular())
                .endereco(req.endereco())
                .email(req.email())
                .senha(passwordEncoder.encode(req.senha()))
                .build();

        clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente registrado com sucesso.");
    }

    // ── UC2 ──────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.senha()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }

        Cliente cliente = clienteRepository.findByEmail(req.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String token = jwtService.generateToken(new ClienteUserDetails(cliente));
        return ResponseEntity.ok(new TokenResponse(token, cliente.getEmail(), cliente.getId()));
    }
}
