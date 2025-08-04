package com.example.codexspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CodexSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodexSpringApplication.class, args);
    }
}
