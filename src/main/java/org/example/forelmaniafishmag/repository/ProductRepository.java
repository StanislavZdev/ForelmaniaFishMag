package org.example.forelmaniafishmag.repository;


import org.example.forelmaniafishmag.tildawebhook.model.ProductsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductsModel,Long> {
    Optional<ProductsModel> findByName(String name);
}
