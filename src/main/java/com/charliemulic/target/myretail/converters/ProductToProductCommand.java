package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductCommand implements Converter<Product, ProductCommand> {

    private final ProductPriceToProductPriceCommand productPriceConverter;

    public ProductToProductCommand(ProductPriceToProductPriceCommand productPriceConverter) {
        this.productPriceConverter = productPriceConverter;
    }

    @Override
    public ProductCommand convert(Product source) {
        if (source == null) {
            return null;
        }

        ProductCommand command = new ProductCommand();
        command.setId(source.getId());
        command.setName(source.getName());
        command.setCurrentPrice(productPriceConverter.convert(source.getCurrentPrice()));

        return command;
    }
}
