package com.bcopstein.deliveryservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliveryServiceApplication {

    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        } catch (Exception ignored) {
        }
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }
}
