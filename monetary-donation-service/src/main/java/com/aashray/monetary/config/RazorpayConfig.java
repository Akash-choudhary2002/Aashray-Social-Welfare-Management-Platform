package com.aashray.monetary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Holds the Razorpay key pair and exposes the plain RestTemplate used to
 * call Razorpay's REST API directly (Orders + Payments endpoints).
 *
 * We deliberately talk to https://api.razorpay.com over REST instead of
 * pulling in the razorpay-java SDK: it's one fewer third-party dependency
 * to version-pin, and the two calls we need (create order, fetch payment)
 * are simple enough that plain HTTP + Jackson covers them fully.
 *
 * IMPORTANT: razorpay.key.secret must NEVER be logged or returned in any
 * API response. Only razorpay.key.id (the public/publishable key) is safe
 * to hand to the frontend, and only via RazorpayOrderResponse.
 */
@Configuration
public class RazorpayConfig {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Bean
    public RestTemplate razorpayRestTemplate() {
        return new RestTemplate();
    }

    public String getKeyId() {
        return keyId;
    }

    public String getKeySecret() {
        return keySecret;
    }
}
