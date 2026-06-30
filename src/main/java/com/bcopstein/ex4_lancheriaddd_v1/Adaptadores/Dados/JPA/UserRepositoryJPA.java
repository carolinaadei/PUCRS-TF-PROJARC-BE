package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.User;

public interface UserRepositoryJPA extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}