
package com.neuromart;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerJavaApplication {

    public static void main(String[] args) {

        // Load .env if available
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // Load .env variables without overriding
        // existing system environment variables
        dotenv.entries().forEach(entry ->
                System.setProperty(
                        entry.getKey(),
                        System.getenv(entry.getKey()) == null
                                ? entry.getValue()
                                : System.getenv(entry.getKey())
                )
        );

        SpringApplication.run(ServerJavaApplication.class, args);
    }
}
