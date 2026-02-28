package org.example.forelmaniafishmag.service;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.repository.ProductRepository;
import org.example.forelmaniafishmag.tildawebhook.dto.TildaWebhookRequestDTO;
import org.example.forelmaniafishmag.tildawebhook.model.ProductsModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Data

public class ProductService {

    private final ProductRepository productRepository;
    CreateProductForProductService createProductForProductService;

    public Set<ProductsModel> getProducts(TildaWebhookRequestDTO dto) {

        Set<ProductsModel> products = new HashSet<>();

        if (dto.getPayment().getProducts() != null) {
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
                    if (existingNames.contains(productDTO.getName())) {

                        ProductsModel product = existingProducts.stream()
                                .filter(p -> p.getName().equals(productDTO.getName()))
                                .findFirst()
                                .orElseGet(() -> createProductForProductService.createProduct(productDTO.getName(), productDTO.getPrice(), productDTO.getQuantity()));
                    } else {
                        ProductsModel product = createProductForProductService.createProduct(productDTO.getName(), productDTO.getPrice(), productDTO.getQuantity());

                        products.add(product);
                    }
                }
            }
        }
        return products;
    }
}
