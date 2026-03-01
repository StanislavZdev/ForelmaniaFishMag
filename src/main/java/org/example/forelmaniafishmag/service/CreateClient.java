package org.example.forelmaniafishmag.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.forelmaniafishmag.repository.ClientRepository;
import org.example.forelmaniafishmag.tildawebhook.model.ClientsModel;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service

public class CreateClient {

    private final ClientRepository clientRepository;

    public ClientsModel findOrCreateClient(String name, String email, String phone) {
        return clientRepository.findByPhone(phone)
                .map(client -> {
                    if (name != null && !name.equals(client.getName())) {
                        client.setName(name);
                    }
                    return client;
                })
                .orElseGet(() -> {
                    log.info("Creating new client: {}", phone);
                    return clientRepository.save(
                            ClientsModel.builder()
                                    .name(name)
                                    .email(email)
                                    .phone(phone)
                                    .build()
                    );
                });
    }
}
