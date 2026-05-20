package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AuthResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.UserRepositoryPort;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepositoryPort repositorio;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthService(UserRepositoryPort repositorio, PasswordEncoder encoder,
                       JwtService jwtService, AuthenticationManager authManager) {
        this.repositorio = repositorio;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public AuthResponse register(String email, String password) {
        User user = new User(0, email, encoder.encode(password), "ROLE_USER");  
        
        if (repositorio.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }
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