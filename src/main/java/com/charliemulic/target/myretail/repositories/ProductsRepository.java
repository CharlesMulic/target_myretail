package com.charliemulic.target.myretail.repositories;

import com.charliemulic.target.myretail.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductsRepository extends MongoRepository<Product, String> {
}
