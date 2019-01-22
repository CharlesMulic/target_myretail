package com.charliemulic.target.myretail.bootstrap;

import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.model.ProductPrice;
import com.charliemulic.target.myretail.repositories.ProductsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData  implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(BootstrapData.class);

    private static Long productCounter = 1L;

    private final ProductsRepository productsRepository;

    public BootstrapData(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        productsRepository.deleteAll();
        log.info("Deleted all products");

        createProduct("Test Product 1", 1.23);
        createProduct("Test Product 2", 2.34);
        createProduct("Test Product 3", 3.45);
    }

    private void createProduct(String name, Double price) {
        Product p = new Product();
        p.setId(productCounter++);
        p.setName(name);

        ProductPrice cp = new ProductPrice();
        cp.setCurrencyCode("USD");
        cp.setValue(price);

        p.setCurrentPrince(cp);
        productsRepository.save(p);
        log.info(String.format("Saved product: %s", name));
    }
}
