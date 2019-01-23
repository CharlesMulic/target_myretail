package com.charliemulic.target.myretail.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product {

    @Id
    private Long id;
    private String name;
    private ProductPrice currentPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ProductPrice currentPrice) {
        this.currentPrice = currentPrice;
    }
}
