package org.example.forelmaniafishmag.service;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.repository.ClientRepository;
import org.example.forelmaniafishmag.repository.ProductRepository;
import org.example.forelmaniafishmag.repository.TildaWebhookDataRepository;
import org.example.forelmaniafishmag.tildawebhook.dto.TildaWebhookRequestDTO;
import org.example.forelmaniafishmag.tildawebhook.model.ClientsModel;
import org.example.forelmaniafishmag.tildawebhook.model.ProductsModel;
import org.example.forelmaniafishmag.tildawebhook.model.TildaWebhookData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Data

public class ForelmaniaFishMagWebhookService {

    private final TildaWebhookDataRepository tildaWebhookDataRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    NumParser numParser;
    CreateClient createClient;
    ProductService productService;

    public TildaWebhookData tildaWebhookOrders(TildaWebhookRequestDTO dto) {
        log.info("Hook processing: {}", dto);

        ClientsModel client = createClient.findOrCreateClient(dto.getName(), dto.getPhone(), dto.getEmail());

        Set<ProductsModel> products = productService.getProducts(dto);

        Double subtotal = numParser.parseDouble(dto.getPayment() != null ? dto.getPayment().getSubtotal() : null);
        Double deliveryPrice = numParser.parseDouble(dto.getPayment() != null ? dto.getPayment().getDeliveryPrice() : null);
        String orderId = dto.getPayment() != null ? dto.getPayment().getOrderId() : null;
        String delivery = dto.getPayment() != null ? dto.getPayment().getDelivery() : null;

        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("orderId is required");
        }
        if (delivery == null || delivery.trim().isEmpty()) {
            throw new IllegalArgumentException("delivery is required");
        }

        TildaWebhookData tildaWebhookData = TildaWebhookData.builder()
                .client(client)
                .delivery(delivery)
                .deliveryPrice(deliveryPrice)
                .orderId(orderId)
                .subtotal(subtotal)
                .submittedAt(LocalDateTime.now())
                .build();
        products.forEach(tildaWebhookData::addProduct);

        TildaWebhookData saved = tildaWebhookDataRepository.save(tildaWebhookData);
        log.info("Webhook saved with ID: {}", saved.getId());
        // возвращаем tildaWebhookOrders, в дальнейшем клиенту или сервису
        return saved;
    }
}

