package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Security;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Getter
public class ClienteUserDetails implements UserDetails {

    private final Long clienteId;
    private final String email;
    private final String senhaHash;

    public ClienteUserDetails(Cliente cliente) {
        this.clienteId = cliente.getId();
        this.email = cliente.getEmail();
        this.senhaHash = cliente.getSenha();
    }

    @Override public String getUsername()               { return email; }
    @Override public String getPassword()               { return senhaHash; }
    @Override public boolean isAccountNonExpired()      { return true; }
    @Override public boolean isAccountNonLocked()       { return true; }
    @Override public boolean isCredentialsNonExpired()  { return true; }
    @Override public boolean isEnabled()                { return true; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
}
