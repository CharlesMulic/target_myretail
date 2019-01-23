package com.charliemulic.target.myretail.bootstrap;

import com.charliemulic.target.myretail.repositories.ProductsRepository;
import com.charliemulic.target.myretail.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class BootstrapData implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(BootstrapData.class);

    private final ProductsRepository productsRepository;
    private final ProductService productService;

    public BootstrapData(ProductsRepository productsRepository, ProductService productService) {
        this.productsRepository = productsRepository;
        this.productService = productService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        rebuildDatabase();
    }

    private void rebuildDatabase() {
        productsRepository.deleteAll();
        log.info("Deleted all products");

        CompletableFuture[] futures = new CompletableFuture[]{
                productService.copyProductDetailsForId("13860420"),
                productService.copyProductDetailsForId("13860421"),
                productService.copyProductDetailsForId("13860424"),
                productService.copyProductDetailsForId("13860425"),
                productService.copyProductDetailsForId("13860428"),
                productService.copyProductDetailsForId("13860429")
        };

        CompletableFuture.allOf(futures).join();
    }

}
