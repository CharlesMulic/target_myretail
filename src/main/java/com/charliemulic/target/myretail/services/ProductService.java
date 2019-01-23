package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.model.tcin.Tcin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {

    Product getProductById(String id);

    ProductCommand saveProductCommand(ProductCommand product);

    CompletableFuture<Boolean> copyProductDetailsForId(String id);

    CompletableFuture<String> getProductName(String id);

    CompletableFuture<Double> getPriceFromDb(String id);

    List<Product> getAllProducts();
}
