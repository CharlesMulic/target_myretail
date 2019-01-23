package com.charliemulic.target.myretail.controllers;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.errors.EntityNotFoundException;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.model.ProductPrice;
import com.charliemulic.target.myretail.services.ProductService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {

    private static final String PRODUCT_ID = "1";
    private static final String PRODUCT_NAME = "Test Product";
    private static final String PRODUCT_CURRENCY_CODE = "USD";
    private static final double PRODUCT_PRICE = 1.23;

    @Mock
    ProductService service;

    ProductController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new ProductController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getAllProductsTest() throws Exception {
        // given
        Product testProduct = createTestProduct();

        // when
        when(service.getAllProducts()).thenReturn(Lists.newArrayList(testProduct));

        // then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(content().string("[{\"id\":\"1\",\"name\":\"Test Product\",\"currentPrice\":{\"value\":1.23,\"currencyCode\":\"USD\"}}]"))
                .andExpect(status().isOk());
    }

    @Test
    public void getProductByIdTest() throws Exception {
        // given
        Product testProduct = createTestProduct();

        // when
        when(service.getProductById(PRODUCT_ID)).thenReturn(testProduct);

        // then
        mockMvc.perform(get(String.format("/api/v1/products/%s", PRODUCT_ID)))
                .andExpect(content().string("{\"id\":\"1\",\"name\":\"Test Product\",\"currentPrice\":{\"value\":1.23,\"currencyCode\":\"USD\"}}"))
                .andExpect(status().isOk());
    }

    @Test
    public void getProductByIdThatDoesntExistTest() throws Exception {
        // when
        when(service.getProductById(PRODUCT_ID)).thenThrow(new EntityNotFoundException());

        // then
        mockMvc.perform(get(String.format("/api/v1/products/%s", PRODUCT_ID))).andExpect(status().is(404));
    }

    @Test
    public void updateProductByIdTest() throws Exception {
        ProductCommand unsavedCommand = new ProductCommand();
        unsavedCommand.setId("1");
        when(service.saveProductCommand(any())).thenReturn(unsavedCommand);

        // then
        mockMvc.perform(put(String.format("/api/v1/products/%s", PRODUCT_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"name\":\"Test Product\",\"currentPrice\":{\"value\":1.23,\"currencyCode\":\"USD\"}}"))
                .andExpect(redirectedUrl(String.format("/api/v1/products/%s", unsavedCommand.getId()))) // is null?
                .andExpect(status().is(302));
    }

    @Test
    public void getProductNameAndPriceTest() throws Exception {
        when(service.getProductName(any())).thenReturn(CompletableFuture.completedFuture("Test Name"));
        when(service.getPriceFromDb(any())).thenReturn(CompletableFuture.completedFuture(1.23));

        // then
        mockMvc.perform(get(String.format("/api/v1/products/%s/name", PRODUCT_ID)))
                .andExpect(content().string("{\"price\":1.23,\"name\":\"Test Name\",\"id\":\"1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void copyProductDataToLocalDbTest() throws Exception {
        when(service.copyProductDetailsForId(any())).thenReturn(CompletableFuture.completedFuture(true));

        // then
        mockMvc.perform(get(String.format("/api/v1/products/%s/copy", PRODUCT_ID)))
                .andExpect(content().string("Data Copied Successfully"))
                .andExpect(status().isOk());
    }

    @Test
    public void copyProductDataToLocalDbFailureTest() throws Exception {
        when(service.copyProductDetailsForId(any())).thenReturn(CompletableFuture.completedFuture(false));

        // then
        mockMvc.perform(get(String.format("/api/v1/products/%s/copy", PRODUCT_ID)))
//                .andExpect(content().string("Unable to retrieve data for id: 1"))
                .andExpect(status().is(404));
    }

    private Product createTestProduct() {
        Product testProduct = new Product();
        testProduct.setId(PRODUCT_ID);
        testProduct.setName(PRODUCT_NAME);

        ProductPrice price = new ProductPrice();
        price.setCurrencyCode(PRODUCT_CURRENCY_CODE);
        price.setValue(PRODUCT_PRICE);
        testProduct.setCurrentPrice(price);
        return testProduct;
    }
}