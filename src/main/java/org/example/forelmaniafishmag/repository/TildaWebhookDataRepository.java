package org.example.forelmaniafishmag.repository;


import org.example.forelmaniafishmag.tildawebhook.model.TildaWebhookData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TildaWebhookDataRepository extends JpaRepository<TildaWebhookData, Long> {
}
