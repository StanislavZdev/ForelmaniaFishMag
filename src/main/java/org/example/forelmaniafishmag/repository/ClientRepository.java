package org.example.forelmaniafishmag.repository;


import org.example.forelmaniafishmag.tildawebhook.model.ClientsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientsModel, Long> {
    Optional<ClientsModel> findByNameAndPhone(String name, String phone);
}
