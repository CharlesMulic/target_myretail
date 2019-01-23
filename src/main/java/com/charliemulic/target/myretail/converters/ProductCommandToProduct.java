package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.model.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ProductCommandToProduct implements Converter<ProductCommand, Product> {

    private final ProductPriceCommandToProductPrice productPriceConverter;

    public ProductCommandToProduct(ProductPriceCommandToProductPrice productPriceConverter) {
        this.productPriceConverter = productPriceConverter;
    }

    @Nullable
    @Override
    public Product convert(ProductCommand source) {
        if (source == null) {
            return null;
        }

        final Product product = new Product();
        product.setId(source.getId());
        product.setName(source.getName());
        product.setCurrentPrice(productPriceConverter.convert(source.getCurrentPrice()));

        return product;
    }
}
