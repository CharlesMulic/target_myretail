package com.charliemulic.target.myretail.model.tcin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductDescription {

    @JsonProperty("title")
    private String title;

    public ProductDescription() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
