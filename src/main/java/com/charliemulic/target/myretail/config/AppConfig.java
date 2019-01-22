package com.charliemulic.target.myretail.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("");
    }
}
