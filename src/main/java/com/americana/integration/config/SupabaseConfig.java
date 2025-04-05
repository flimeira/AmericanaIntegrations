package com.americana.integration.config;

import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.createSupabaseClient;
import io.github.jan.supabase.postgrest.Postgrest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean(name = "deliverySupabaseClient")
    public SupabaseClient deliverySupabaseClient() {
        return createSupabaseClient(
            deliveryUrl,
            deliveryKey,
            config -> {
                config.install(Postgrest);
                return config;
            }
        );
    }

    @Bean(name = "restaurantSupabaseClient")
    public SupabaseClient restaurantSupabaseClient() {
        return createSupabaseClient(
            restaurantUrl,
            restaurantKey,
            config -> {
                config.install(Postgrest);
                return config;
            }
        );
    }
} 