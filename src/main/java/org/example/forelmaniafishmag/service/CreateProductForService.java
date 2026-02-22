package org.example.forelmaniafishmag.service;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.repository.ProductRepository;
import org.example.forelmaniafishmag.tildawebhook.model.ProductsModel;

@Slf4j
@Data
public class CreateProductForService {

    private final ProductRepository productRepository;

    NumParser numParser;

    public ProductsModel createProduct(String name, String priceStr, String quantityStr) {
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
