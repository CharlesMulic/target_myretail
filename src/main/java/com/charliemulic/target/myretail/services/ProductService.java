package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.model.Product;

import java.util.concurrent.CompletableFuture;

public interface ProductService {

    Product getProductById(Long id);

    ProductCommand saveProductCommand(ProductCommand product);

    CompletableFuture<String> getProductName(Long id);

    CompletableFuture<Double> getPriceFromDb(Long id);
}
