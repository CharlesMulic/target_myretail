package com.charliemulic.target.myretail.commands;

import javax.validation.constraints.NotNull;

public class ProductPriceCommand {

    @NotNull
    private Double value;

    @NotNull
    private String currencyCode;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
