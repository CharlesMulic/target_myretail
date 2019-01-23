package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductPriceCommand;
import com.charliemulic.target.myretail.model.ProductPrice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductPriceCommandToProductPriceTest {
    private ProductPriceCommandToProductPrice converter;

    @Before
    public void setUp() {
        converter = new ProductPriceCommandToProductPrice();
    }

    @Test
    public void canConvertNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void canConvertEmptyObject() {
        assertNotNull(converter.convert(new ProductPriceCommand()));
    }

    @Test
    public void convert() {
        // given
        ProductPriceCommand command = new ProductPriceCommand();
        command.setCurrencyCode("USD");
        command.setValue(1.5);

        // when
        ProductPrice price = converter.convert(command);

        // then
        assertEquals("USD", price.getCurrencyCode());
        assertEquals(Double.valueOf(1.5), price.getValue());
    }
}