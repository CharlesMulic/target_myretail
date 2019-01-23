package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductPriceCommand;
import com.charliemulic.target.myretail.model.ProductPrice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductPriceToProductPriceCommandTest {
    private ProductPriceToProductPriceCommand converter;

    @Before
    public void setUp() {
        converter = new ProductPriceToProductPriceCommand();
    }

    @Test
    public void canConvertNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void canConvertEmptyObject() {
        ProductPriceCommand command = converter.convert(new ProductPrice());
        assertNotNull(command);
        assertNull(command.getCurrencyCode());
        assertNull(command.getValue());
    }

    @Test
    public void convert() {
        // given
        ProductPrice price = new ProductPrice();
        price.setCurrencyCode("USD");
        price.setValue(1.5);

        // when
        ProductPriceCommand command = converter.convert(price);

        // then
        assertEquals("USD", command.getCurrencyCode());
        assertEquals(Double.valueOf(1.5), command.getValue());
    }
}