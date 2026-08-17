package com.aashray.monetary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MonetaryDonationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonetaryDonationServiceApplication.class, args);
    }
}
