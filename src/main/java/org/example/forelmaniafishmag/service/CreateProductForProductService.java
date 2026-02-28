package org.example.forelmaniafishmag.service;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.repository.ProductRepository;
import org.example.forelmaniafishmag.tildawebhook.model.ProductsModel;

@Slf4j
@Data
public class CreateProductForProductService {

    private final ProductRepository productRepository;

    NumParser numParser;

    public ProductsModel createProduct(String name, String priceStr, String quantityStr) {

        Double price = numParser.parseDouble(priceStr);
        Integer quantity = numParser.parseInteger(quantityStr);

        log.info("Created new product: {}", name);

        return productRepository.save(ProductsModel.builder()
                .name(name)
                .price(price != null ? price : 0.0)
                .quantity(quantity != null ? quantity : 0)
                .build());
    }
}
