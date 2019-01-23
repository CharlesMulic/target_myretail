package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.commands.ProductPriceCommand;
import com.charliemulic.target.myretail.model.Product;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductCommandToProductTest {

    private ProductCommandToProduct converter;
    private ProductPriceCommand productPriceCommand;

    @Before
    public void setUp() {
        converter = new ProductCommandToProduct(new ProductPriceCommandToProductPrice());
        productPriceCommand = new ProductPriceCommand();
        productPriceCommand.setValue(1.5);
        productPriceCommand.setCurrencyCode("USD");
    }

    @Test
    public void canConvertNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void canConvertEmptyObject() {
        assertNotNull(converter.convert(new ProductCommand()));
    }

    @Test
    public void convert() {
        // given
        ProductCommand command = new ProductCommand();
        command.setName("Test Name");
        command.setId("1");
        command.setCurrentPrice(productPriceCommand);

        // when
        Product product = converter.convert(command);

        // then
        assertEquals("Test Name", product.getName());
        assertEquals("1", product.getId());
        assertNotNull(product.getCurrentPrice());
    }
}