package com.charliemulic.target.myretail.commands;


public class ProductCommand {

    private Long id;
    private String name;
    private ProductPriceCommand currentPrice;

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

    public ProductPriceCommand getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ProductPriceCommand currentPrice) {
        this.currentPrice = currentPrice;
    }
}
