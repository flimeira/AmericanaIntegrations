package com.americana.integration.controller;

import com.americana.integration.service.OrderIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final OrderIntegrationService orderIntegrationService;

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
} 