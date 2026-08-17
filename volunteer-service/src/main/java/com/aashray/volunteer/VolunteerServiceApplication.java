package com.aashray.volunteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VolunteerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VolunteerServiceApplication.class, args);
    }
}
