package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.commands.ProductPriceCommand;
import com.charliemulic.target.myretail.converters.ProductCommandToProduct;
import com.charliemulic.target.myretail.converters.ProductToProductCommand;
import com.charliemulic.target.myretail.errors.EntityNotFoundException;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.model.tcin.Tcin;
import com.charliemulic.target.myretail.repositories.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private static final String TCIN_ENDPOINT = "https://redsky.target.com/v2/pdp/tcin";
    private static final String TCIN_PARAMS = "excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    private final ProductsRepository productsRepository;
    private final ProductCommandToProduct productCommandToProduct;
    private final ProductToProductCommand productToProductCommand;
    private final RestTemplate restTemplate;

    public ProductServiceImpl(ProductsRepository productsRepository, ProductCommandToProduct productCommandToProduct, ProductToProductCommand productToProductCommand, RestTemplate restTemplate) {
        this.productsRepository = productsRepository;
        this.productCommandToProduct = productCommandToProduct;
        this.productToProductCommand = productToProductCommand;
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(String id) {
        Product product = productsRepository.findById(id).orElse(null);
        if (product == null) {
            throw new EntityNotFoundException();
        }
        return product;
    }

    @Override
    public ProductCommand saveProductCommand(ProductCommand command) {
        Product unsavedProduct = productCommandToProduct.convert(command);
        Product savedProduct = productsRepository.save(unsavedProduct);
        log.info(String.format("Saved product with id: %s", savedProduct.getId()));
        return productToProductCommand.convert(savedProduct);
    }

    /**
     * Asynchronously retrieves a product's name given its id from a third party API.
     *
     * @param id - The id of the product
     * @return - The name of the product
     */
    @Async
    @Override
    public CompletableFuture<String> getProductName(String id) {
        try {
            log.info(String.format("Fetching product %s details from: %s", id, TCIN_ENDPOINT));
            Tcin response = restTemplate.getForObject(String.format("%s/%s?%s", TCIN_ENDPOINT, id, TCIN_PARAMS), Tcin.class);
            return CompletableFuture.completedFuture(response.getProduct().getItem().getProductDescription().getTitle());
        } catch (HttpClientErrorException e) {
            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    @Override
    public CompletableFuture<Void> copyProductDetailsForId(String id) {
        log.info(String.format("Attempting to copy product information from third party with id: %s to local application...", id));

        Tcin details;
        try {
            log.info(String.format("Fetching product %s details from: %s", id, TCIN_ENDPOINT));
            details = restTemplate.getForObject(String.format("%s/%s?%s", TCIN_ENDPOINT, id, TCIN_PARAMS), Tcin.class);
        } catch (HttpClientErrorException e) {
            return CompletableFuture.completedFuture(null);
        }
        ProductCommand product = new ProductCommand();
        product.setId(details.getProduct().getItem().getTcin());
        product.setName(details.getProduct().getItem().getProductDescription().getTitle());

        ProductPriceCommand price = new ProductPriceCommand();
        price.setCurrencyCode("USD");
        price.setValue(details.getProduct().getPrice().getOfferPrice().getPrice());
        product.setCurrentPrice(price);

        saveProductCommand(product);
        log.info(String.format("Successfully copied external data for product id: %s", id));
        return CompletableFuture.completedFuture(null);
    }

    /**
     * Asynchronously retrieves the price of a product with the given id
     *
     * @param id - The id of the product
     * @return - The price of the product
     */
    @Async
    @Override
    public CompletableFuture<Double> getPriceFromDb(String id) {
        log.info(String.format("Fetching product pricing information from database for product id: %s", id));
        Product product = getProductById(id);
        return CompletableFuture.completedFuture(product.getCurrentPrice().getValue());
    }

    @Override
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }
}
