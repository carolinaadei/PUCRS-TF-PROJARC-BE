package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.UserRepositoryJPA;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AuthResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepositoryJPA repositorio;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Autowired
    public AuthService(UserRepositoryJPA repositorio,
                       PasswordEncoder encoder,
                       JwtService jwtService,
                       AuthenticationManager authManager) {
        this.repositorio = repositorio;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public AuthResponse register(String email, String password) {
        if (repositorio.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole("ROLE_USER");
        repositorio.save(user);
        return new AuthResponse(jwtService.createToken(user));
    }

    public AuthResponse login(String email, String password) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        User user = repositorio.findByEmail(email).orElseThrow();
        return new AuthResponse(jwtService.createToken(user));
    }
}
