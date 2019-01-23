package com.charliemulic.target.myretail.model.tcin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {

    @JsonProperty("product_description")
    private ProductDescription productDescription;

    public Item() {}

    public ProductDescription getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(ProductDescription productDescription) {
        this.productDescription = productDescription;
    }
}