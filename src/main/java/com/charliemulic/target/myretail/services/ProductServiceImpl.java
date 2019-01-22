package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.repositories.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductsRepository productsRepository;

    public ProductServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productsRepository.findById(id);
    }
}
