package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepositoryPort {

    private final JdbcTemplate jdbc;

    public UserRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User u = jdbc.queryForObject(
                "SELECT * FROM usuarios WHERE email = ?",
                (rs, row) -> new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                ),
                email
            );
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

@Override
public User save(User user) {
    jdbc.update(
        "INSERT INTO usuarios (email, password, role) VALUES (?, ?, ?)",
        user.getEmail(),
        user.getPassword(),
        user.getRole()
    );
    return user;
}
}