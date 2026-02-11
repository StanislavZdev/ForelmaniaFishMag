package org.example.forelmaniafishmag.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.repository.ClientRepository;
import org.example.forelmaniafishmag.repository.ProductRepository;
import org.example.forelmaniafishmag.repository.TildaWebhookDataRepository;
import org.example.forelmaniafishmag.tildawebhook.dto.TildaWebhookRequestDTO;
import org.example.forelmaniafishmag.tildawebhook.model.TildaWebhookData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional

public class ForelmaniaFishMagWebhookService {

    private final TildaWebhookDataRepository tildaWebhookDataRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public TildaWebhookData processWebhook(TildaWebhookRequestDTO dto) {
        log.info("Processing Tilda webhook: {}", dto);

        String name = dto.getName();
        String email = dto.getEmail();
        String phone = dto.getPhone();

        if (name == null || phone == null || name.isBlank() || phone.isBlank()) {
            throw new IllegalArgumentException("Name or Phone is required");
        }

    }
}
