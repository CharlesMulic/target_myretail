package com.charliemulic.target.myretail.model.tcin;

/*
 Domain class for mapping some of the JSON data returned from the endpoint:
 http://redsky.target.com/v2/pdp/tcin/{id}
 */
public class Tcin {

    private Product product;

    public Tcin() {}

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
