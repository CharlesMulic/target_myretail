package com.charliemulic.target.myretail.model.tcin;

import java.io.Serializable;

/*
 Domain class for mapping some of the JSON data returned from the endpoint:
 http://redsky.target.com/v2/pdp/tcin/{id}
 */
public class Tcin implements Serializable {

    private Product product;

    public Tcin() {}

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
