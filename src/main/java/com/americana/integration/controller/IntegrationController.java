package com.americana.integration.controller;

import com.americana.integration.service.OrderIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final OrderIntegrationService orderIntegrationService;
    private final WebClient deliveryWebClient;
    private final WebClient restaurantWebClient;

    @PostMapping("/sync")
    public ResponseEntity<String> triggerSync() {
        try {
            orderIntegrationService.syncOrders();
            return ResponseEntity.ok("Sincronização iniciada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Erro ao iniciar sincronização: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Serviço de integração está funcionando");
    }

    @GetMapping("/test-connections")
    public ResponseEntity<String> testConnections() {
        try {
            // Testa conexão com Supabase do Delivery
            String deliveryResponse = deliveryWebClient
                .get()
                .uri("/rest/v1/orders?limit=1")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Erro ao conectar com Delivery: " + e.getMessage()))
                .block();

            // Testa conexão com Supabase do Restaurant
            String restaurantResponse = restaurantWebClient
                .get()
                .uri("/rest/v1/orders?limit=1")
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Erro ao conectar com Restaurant: " + e.getMessage()))
                .block();

            return ResponseEntity.ok("Resultado dos testes:\nDelivery: " + deliveryResponse + "\nRestaurant: " + restaurantResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Erro ao testar conexões: " + e.getMessage());
        }
    }
} 