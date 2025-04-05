package com.americana.integration.service;

import com.americana.integration.config.RabbitMQConfig;
import com.americana.integration.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderIntegrationService {

    private final WebClient deliveryWebClient;
    private final WebClient restaurantWebClient;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 30000) // Executa a cada 30 segundos
    public void syncOrders() {
        try {
            // Busca pedidos novos ou atualizados no Supabase do Delivery
            List<Order> deliveryOrders = deliveryWebClient
                .get()
                .uri("/rest/v1/orders?updated_at=gte." + LocalDateTime.now().minusMinutes(5))
                .retrieve()
                .bodyToMono(List.class)
                .block();

            // Processa cada pedido
            if (deliveryOrders != null) {
                for (Order order : deliveryOrders) {
                    // Envia para a fila do RabbitMQ
                    rabbitTemplate.convertAndSend(
                        RabbitMQConfig.ORDER_EXCHANGE,
                        RabbitMQConfig.ORDER_ROUTING_KEY,
                        order
                    );
                }
            }

            // Busca atualizações de status no Supabase do Restaurant
            List<Order> restaurantOrders = restaurantWebClient
                .get()
                .uri("/rest/v1/orders?updated_at=gte." + LocalDateTime.now().minusMinutes(5))
                .retrieve()
                .bodyToMono(List.class)
                .block();

            // Atualiza os pedidos no Supabase do Delivery
            if (restaurantOrders != null) {
                for (Order order : restaurantOrders) {
                    deliveryWebClient
                        .patch()
                        .uri("/rest/v1/orders?id=eq." + order.getId())
                        .bodyValue(order)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                }
            }
        } catch (Exception e) {
            log.error("Erro ao sincronizar pedidos", e);
        }
    }

    public void processOrder(Order order) {
        try {
            // Atualiza o pedido no Supabase do Restaurant
            restaurantWebClient
                .patch()
                .uri("/rest/v1/orders?id=eq." + order.getId())
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
            log.error("Erro ao processar pedido", e);
        }
    }
} 