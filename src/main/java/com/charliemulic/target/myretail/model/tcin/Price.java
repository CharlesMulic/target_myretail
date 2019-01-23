package com.charliemulic.target.myretail.model.tcin;

public class Price {

    private ListPrice listPrice;

    private OfferPrice offerPrice;

    public Price() {}

    public ListPrice getListPrice() {
        return listPrice;
    }

    public void setListPrice(ListPrice listPrice) {
        this.listPrice = listPrice;
    }

    public OfferPrice getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(OfferPrice offerPrice) {
        this.offerPrice = offerPrice;
    }
}
