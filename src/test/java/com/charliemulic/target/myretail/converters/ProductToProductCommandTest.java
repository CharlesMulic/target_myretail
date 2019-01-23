package com.charliemulic.target.myretail.converters;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.model.ProductPrice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductToProductCommandTest {
    private ProductToProductCommand converter;

    @Before
    public void setUp() {
        converter = new ProductToProductCommand(new ProductPriceToProductPriceCommand());
    }

    @Test
    public void canConvertNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void canConvertEmptyObject() {
        ProductCommand command = converter.convert(new Product());
        assertNotNull(command);
        assertNull(command.getId());
        assertNull(command.getCurrentPrice());
        assertNull(command.getName());
    }

    @Test
    public void convert() {
        // given
        Product product = new Product();
        product.setName("Test Name");
        product.setId("1");
        product.setCurrentPrice(new ProductPrice());

        // when
        ProductCommand command = converter.convert(product);

        // then
        assertEquals("Test Name", command.getName());
        assertEquals("1", command.getId());
        assertNotNull(command.getCurrentPrice());
    }
}