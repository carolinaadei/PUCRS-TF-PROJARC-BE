package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados.JPA.UserRepositoryJPA;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepositoryJPA repositorio;

    public UserDetailService(UserRepositoryJPA repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repositorio.findByEmail  (email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}