package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.model.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Optional<Product> getProductById(Integer id) {
//        return Optional.empty();
        Product p = new Product();
        p.setId(1L);
        return Optional.of(p);
    }
}
