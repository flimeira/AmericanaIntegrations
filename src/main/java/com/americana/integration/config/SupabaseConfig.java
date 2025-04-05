package com.americana.integration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SupabaseConfig {

    @Value("${supabase.delivery.url}")
    private String deliveryUrl;

    @Value("${supabase.delivery.key}")
    private String deliveryKey;

    @Value("${supabase.restaurant.url}")
    private String restaurantUrl;

    @Value("${supabase.restaurant.key}")
    private String restaurantKey;

    @Bean(name = "deliveryWebClient")
    public WebClient deliveryWebClient() {
        return WebClient.builder()
            .baseUrl(deliveryUrl)
            .defaultHeader("apikey", deliveryKey)
            .defaultHeader("Authorization", "Bearer " + deliveryKey)
            .build();
    }

    @Bean(name = "restaurantWebClient")
    public WebClient restaurantWebClient() {
        return WebClient.builder()
            .baseUrl(restaurantUrl)
            .defaultHeader("apikey", restaurantKey)
            .defaultHeader("Authorization", "Bearer " + restaurantKey)
            .build();
    }
} 