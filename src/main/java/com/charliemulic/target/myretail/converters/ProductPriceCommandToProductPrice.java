package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductPriceCommand;
import com.charliemulic.target.myretail.model.ProductPrice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceCommandToProductPrice implements Converter<ProductPriceCommand, ProductPrice> {
    @Override
    public ProductPrice convert(ProductPriceCommand source) {
        if (source == null) {
            return null;
        }

        final ProductPrice price = new ProductPrice();
        price.setValue(source.getValue());
        price.setCurrencyCode(source.getCurrencyCode());
        return price;
    }
}
