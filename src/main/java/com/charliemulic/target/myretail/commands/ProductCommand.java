package com.charliemulic.target.myretail.commands;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ProductCommand {

    private String id;

    @NotNull
    private String name;

    @Valid
    private ProductPriceCommand currentPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
