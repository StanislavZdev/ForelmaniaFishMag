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
import java.util.HashSet;
import java.util.List;
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
    CreateClientForService createClientForService;
    CreateProductForService createProductForService;

    public TildaWebhookData tildaWebhookOrders(TildaWebhookRequestDTO dto) {
        log.info("Hook processing: {}", dto);

            ClientsModel client = createClientForService.findOrCreateClient(dto.getName(), dto.getPhone(), dto.getEmail());

            Set<ProductsModel> products = new HashSet<>();
            if (dto.getPayment() != null && dto.getPayment().getProducts() != null) {
                List<String> productNames = dto.getPayment().getProducts().stream()
                        .map(TildaWebhookRequestDTO.PaymentDTO.ProductDTO::getName)
                        // дополнительная валидация name продукта
                        .filter(name -> name != null && !name.trim().isEmpty())
                        .toList();

                if (!productNames.isEmpty()) {
                    List<ProductsModel> existingProducts = productRepository.findAllByNameIn(productNames);
                    Set<String> existingNames = existingProducts.stream()
                            .map(ProductsModel::getName)
                            .collect(HashSet::new, HashSet::add, HashSet::addAll);

                    for (TildaWebhookRequestDTO.PaymentDTO.ProductDTO productDTO : dto.getPayment().getProducts()) {
                        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
                            continue;
                        }

                        ProductsModel product = existingProducts.stream()
                                .filter(p -> p.getName().equals(productDTO.getName()))
                                .findFirst()
                                .orElseGet(() -> createProduct(productDTO.getName(), productDTO.getPrice(), productDTO.getQuantity()));

                        products.add(product);
                    }
                }
            }
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
            return saved;
        }
        ProductsModel createProduct (String name, String priceStr, String quantityStr){
            Double price = numParser.parseDouble(priceStr);
            Integer quantity = numParser.parseInteger(quantityStr);

            ProductsModel product = ProductsModel.builder()
                    .name(name)
                    .price(price != null ? price : 0.0)
                    .quantity(quantity != null ? quantity : 0)
                    .build();

            ProductsModel saved = productRepository.save(product);
            log.info("Created new product: {} (ID: {})", saved.getName(), saved.getId());
            return saved;
        }
    }

