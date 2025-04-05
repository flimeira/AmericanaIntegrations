package com.americana.integration.service;

import com.americana.integration.config.RabbitMQConfig;
import com.americana.integration.model.Order;
import io.github.jan.supabase.SupabaseClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderIntegrationService {

    private final SupabaseClient deliverySupabaseClient;
    private final SupabaseClient restaurantSupabaseClient;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 30000) // Executa a cada 30 segundos
    public void syncOrders() {
        try {
            // Busca pedidos novos ou atualizados no Supabase do Delivery
            var deliveryOrders = deliverySupabaseClient
                .from("orders")
                .select()
                .gte("updated_at", LocalDateTime.now().minusMinutes(5))
                .execute();

            // Processa cada pedido
            for (var order : deliveryOrders) {
                // Envia para a fila do RabbitMQ
                rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    RabbitMQConfig.ORDER_ROUTING_KEY,
                    order
                );
            }

            // Busca atualizações de status no Supabase do Restaurant
            var restaurantOrders = restaurantSupabaseClient
                .from("orders")
                .select()
                .gte("updated_at", LocalDateTime.now().minusMinutes(5))
                .execute();

            // Atualiza os pedidos no Supabase do Delivery
            for (var order : restaurantOrders) {
                deliverySupabaseClient
                    .from("orders")
                    .update(order)
                    .eq("id", order.getId())
                    .execute();
            }

        } catch (Exception e) {
            log.error("Erro ao sincronizar pedidos: ", e);
        }
    }

    public void processOrder(Order order) {
        try {
            // Atualiza o pedido no Supabase do Restaurant
            restaurantSupabaseClient
                .from("orders")
                .upsert(order)
                .execute();

            log.info("Pedido {} processado com sucesso", order.getId());
        } catch (Exception e) {
            log.error("Erro ao processar pedido {}: ", order.getId(), e);
        }
    }
} 