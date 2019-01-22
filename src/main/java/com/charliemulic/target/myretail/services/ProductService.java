package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.model.Product;

import java.util.Optional;

public interface ProductService {

    Optional<Product> getProductById(Long id);
}
