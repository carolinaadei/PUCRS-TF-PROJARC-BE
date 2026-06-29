package com.pizzaria.gateway.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Login e registro de usuários ficam no gateway: ele é quem emite e valida os
 * tokens JWT agora (ver {@link com.pizzaria.gateway.JwtGatewayFilter}). O
 * serviço de pizzaria não roda mais Spring Security nem conhece a tabela
 * "usuarios" — só confia no header X-User-Email que o gateway encaminha.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssuer jwtIssuer;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtIssuer jwtIssuer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtIssuer = jwtIssuer;
    }

    @PostMapping("/registrar")
    public Mono<AuthResponse> register(@RequestBody AuthRequest req) {
        return Mono.fromCallable(() -> {
            if (userRepository.findByEmail(req.email()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
            }
            UserEntity user = new UserEntity(req.email(), passwordEncoder.encode(req.password()), "ROLE_USER");
            userRepository.save(user);
            return new AuthResponse(jwtIssuer.createToken(user));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody AuthRequest req) {
        return Mono.fromCallable(() -> {
            UserEntity user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
            if (!passwordEncoder.matches(req.password(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
            }
            return new AuthResponse(jwtIssuer.createToken(user));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public record AuthRequest(String email, String password) {}
    public record AuthResponse(String token) {}
}
