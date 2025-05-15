package com.clonecod.clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.clonecod.clone")
public class CloneApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloneApplication.class, args);
    }
}