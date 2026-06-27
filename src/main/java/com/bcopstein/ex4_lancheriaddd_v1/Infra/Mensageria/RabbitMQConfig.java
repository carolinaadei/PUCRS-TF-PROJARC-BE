package com.bcopstein.ex4_lancheriaddd_v1.Infra.Mensageria;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME  = "pizzaria.entrega.fila";
    public static final String EXCHANGE    = "pizzaria.entrega.exchange";
    public static final String ROUTING_KEY = "entrega";

    @Bean
    public Queue entregaQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange entregaExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding entregaBinding(Queue entregaQueue, DirectExchange entregaExchange) {
        return BindingBuilder.bind(entregaQueue).to(entregaExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
