package com.aashray.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FoodDonationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodDonationServiceApplication.class, args);
    }
}
