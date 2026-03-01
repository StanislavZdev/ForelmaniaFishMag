package org.example.forelmaniafishmag.service;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.repository.ProductRepository;
import org.example.forelmaniafishmag.tildawebhook.model.ProductsModel;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service

public class CreateProductForProductService {

    private final ProductRepository productRepository;
    private final NumParser numParser;

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
