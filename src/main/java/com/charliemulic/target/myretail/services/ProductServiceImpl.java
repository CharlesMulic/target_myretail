package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.converters.ProductCommandToProduct;
import com.charliemulic.target.myretail.converters.ProductToProductCommand;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.model.tcin.Tcin;
import com.charliemulic.target.myretail.repositories.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductsRepository productsRepository;
    private final ProductCommandToProduct productCommandToProduct;
    private final ProductToProductCommand productToProductCommand;

    public ProductServiceImpl(ProductsRepository productsRepository, ProductCommandToProduct productCommandToProduct, ProductToProductCommand productToProductCommand) {
        this.productsRepository = productsRepository;
        this.productCommandToProduct = productCommandToProduct;
        this.productToProductCommand = productToProductCommand;
    }

    @Override
    public Product getProductById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }

    @Override
    public ProductCommand saveProductCommand(ProductCommand command) {
        Product unsavedProduct = productCommandToProduct.convert(command);
        Product savedProduct = productsRepository.save(unsavedProduct);
        log.info(String.format("Saved product with id: %s", savedProduct.getId()));
        return productToProductCommand.convert(savedProduct);
    }

    @Async
    @Override
    public CompletableFuture<String> getProductName(Long id) {
        RestTemplate restTemplate = new RestTemplate();

        String endpoint = "https://redsky.target.com/v2/pdp/tcin";
//        Long id = 13860428;
        String params = "excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
        try {
            log.info(String.format("Fetching product %d details from: %s", id, endpoint));
            Tcin response = restTemplate.getForObject(String.format("%s/%s?%s", endpoint, id, params), Tcin.class);
            return CompletableFuture.completedFuture(response.getProduct().getItem().getProductDescription().getTitle());
        } catch (HttpClientErrorException e) {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    @Override
    public CompletableFuture<Double> getPriceFromDb(Long id) {
        log.info(String.format("Fetching product pricing information from database for product id: %d", id));
        return CompletableFuture.completedFuture(10.5); // TODO hit database
    }
}
