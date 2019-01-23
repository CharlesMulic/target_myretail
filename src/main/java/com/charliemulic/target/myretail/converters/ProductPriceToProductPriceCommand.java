package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductPriceCommand;
import com.charliemulic.target.myretail.model.ProductPrice;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceToProductPriceCommand implements Converter<ProductPrice, ProductPriceCommand> {
    @Override
    public ProductPriceCommand convert(ProductPrice source) {
        if (source == null) {
            return null;
        }

        ProductPriceCommand command = new ProductPriceCommand();
        command.setValue(source.getValue());
        command.setCurrencyCode(source.getCurrencyCode());

        return command;
    }
}
