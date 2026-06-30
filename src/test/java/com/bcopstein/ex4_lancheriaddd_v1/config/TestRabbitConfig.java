package com.bcopstein.ex4_lancheriaddd_v1.config;

import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestRabbitConfig {

    @Bean
    @ConditionalOnMissingBean(ConnectionFactory.class)
    public ConnectionFactory connectionFactory() {
        return Mockito.mock(ConnectionFactory.class);
    }
}
