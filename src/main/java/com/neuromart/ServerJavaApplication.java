
package com.neuromart;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerJavaApplication {

    public static void main(String[] args) {

        // Load environment variables from .env
        Dotenv dotenv = Dotenv.configure()
                .directory("C:/Users/ujjva/Desktop/NeuroMart/server-java")
                .load();

        // Make variables available to Spring Boot
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(ServerJavaApplication.class, args);
    }
}
