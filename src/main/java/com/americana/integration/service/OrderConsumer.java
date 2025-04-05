package com.americana.integration.service;

import com.americana.integration.config.RabbitMQConfig;
import com.americana.integration.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderIntegrationService orderIntegrationService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void consumeOrder(Order order) {
        try {
            log.info("Recebido pedido {} para processamento", order.getId());
            orderIntegrationService.processOrder(order);
        } catch (Exception e) {
            log.error("Erro ao consumir pedido {}: ", order.getId(), e);
        }
    }
} 