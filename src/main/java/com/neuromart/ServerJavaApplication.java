// package com.neuromart;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class ServerJavaApplication {

// 	public static void main(String[] args) {
// 		SpringApplication.run(ServerJavaApplication.class, args);
// 	}

// }





package com.neuromart;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerJavaApplication {

    public static void main(String[] args) {

        // Load environment variables from .env
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .load();

        // Make variables available to Spring Boot
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(ServerJavaApplication.class, args);
    }
}