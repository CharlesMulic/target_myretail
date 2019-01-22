package com.charliemulic.target.myretail.controllers;

import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@RequestParam Integer id) {
        return productService.getProductById(id).orElse(null);
    }
}
