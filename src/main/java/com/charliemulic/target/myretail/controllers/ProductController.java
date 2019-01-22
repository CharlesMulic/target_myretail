package com.charliemulic.target.myretail.controllers;

import com.charliemulic.target.myretail.errors.EntityNotFoundException;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new EntityNotFoundException();
        }
        return product;
    }
}
