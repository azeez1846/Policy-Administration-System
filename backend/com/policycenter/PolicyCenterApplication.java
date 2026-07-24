package com.policycenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application entry point for Guidewire PolicyCenter Sandbox.
 */
@SpringBootApplication
public class PolicyCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolicyCenterApplication.class, args);
        System.out.println("=================================================================");
        System.out.println(" Guidewire PolicyCenter Spring Boot Server running on http://localhost:8080");
        System.out.println(" LiveReload & DevTools auto-reloading ENABLED.");
        System.out.println("=================================================================");
    }
}
