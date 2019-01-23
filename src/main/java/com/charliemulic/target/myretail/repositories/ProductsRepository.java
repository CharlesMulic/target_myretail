package com.charliemulic.target.myretail.repositories;

import com.charliemulic.target.myretail.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductsRepository extends MongoRepository<Product, Long> {

    Optional<Product> findById(String id);
}
