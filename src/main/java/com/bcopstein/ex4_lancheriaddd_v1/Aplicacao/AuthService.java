package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.UserRepositoryJPA;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AuthResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

@Autowired
    private final UserRepositoryJPA repositorio;
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authManager;

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