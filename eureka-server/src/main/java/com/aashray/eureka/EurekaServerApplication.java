package com.aashray.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Discovery Server.
 * Every microservice (Auth, Food Donation, Monetary Donation, Education,
 * Volunteer, Notification) registers itself here so the API Gateway
 * and other services can discover them by logical name instead of
 * hardcoded host:port.
 *
 * Dashboard: http://localhost:8761
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
