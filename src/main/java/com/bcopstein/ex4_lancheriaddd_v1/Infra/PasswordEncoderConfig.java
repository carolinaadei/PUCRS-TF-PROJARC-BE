package com.bcopstein.ex4_lancheriaddd_v1.Infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Usado apenas para hash da senha do cliente em CadastrarClienteUC.
 * A autenticação/JWT em si foi extraída para o gateway (ver task B1) —
 * este serviço não roda mais Spring Security.
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
