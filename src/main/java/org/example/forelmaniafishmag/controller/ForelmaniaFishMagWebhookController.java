package org.example.forelmaniafishmag.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.service.ForelmaniaFishMagWebhookService;
import org.example.forelmaniafishmag.tildawebhook.dto.TildaWebhookRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
        // надо уточнить /orders
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j

public class ForelmaniaFishMagWebhookController {

    private final ForelmaniaFishMagWebhookService forelmaniaFishMagWebhookService;

    @PostMapping
    public ResponseEntity<String> handleWebhookOrders(@RequestBody TildaWebhookRequestDTO dto) {
        log.info("Received Tilda webhook: {}", dto);

        try {
            forelmaniaFishMagWebhookService.processWebhookOrders(dto);
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error processing webhook", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
}

