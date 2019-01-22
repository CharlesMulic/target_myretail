package com.charliemulic.target.myretail.config;

import com.charliemulic.target.myretail.repositories.ProductsRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = ProductsRepository.class)
@Configuration
public class AppConfig {

    @Value("${spring.data.mongodb.host}")
    private String dbHost;

    @Value("${spring.data.mongodb.username}")
    private String dbUser;

    @Value("${spring.data.mongodb.password}")
    private String dbPassword;

    @Bean
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(String.format("mongodb+srv://%s:%s@%s", dbUser, dbPassword, dbHost));
        return new MongoClient(uri);
    }
}
