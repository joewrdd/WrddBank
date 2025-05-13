package com.example.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//----- Server Application -----
@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        //----- Load Environment Variables From .env File -----
        Dotenv dotenv = Dotenv.configure().load();
        
        //----- Set System Properties For Spring Boot To Use -----
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        
        //----- Run The Spring Boot Application -----
        SpringApplication.run(ServerApplication.class, args);
    }
}