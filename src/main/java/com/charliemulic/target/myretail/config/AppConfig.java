package com.charliemulic.target.myretail.config;

import com.charliemulic.target.myretail.repositories.ProductsRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.Executor;

@EnableMongoRepositories(basePackageClasses = ProductsRepository.class)
@Configuration
public class AppConfig {

    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "embedded_db";

//    @Value("${spring.data.mongodb.host}")
//    private String dbHost;
//
//    @Value("${spring.data.mongodb.username}")
//    private String dbUser;
//
//    @Value("${spring.data.mongodb.password}")
//    private String dbPassword;

//    @Bean
//    public MongoClient mongoClient() {
//        MongoClientURI uri = new MongoClientURI(String.format("mongodb+srv://%s:%s@%s", dbUser, dbPassword, dbHost));
//        return new MongoClient(uri);
//    }

    @Bean
    public MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp(MONGO_DB_URL);
        MongoClient mongoClient = mongo.getObject();
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
        return mongoTemplate;
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("FuturesPool-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
