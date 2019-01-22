package com.charliemulic.target.myretail.config;

import com.charliemulic.target.myretail.repositories.ProductsRepository;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = ProductsRepository.class)
@Configuration
public class AppConfig {

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("localhost");
    }
}
