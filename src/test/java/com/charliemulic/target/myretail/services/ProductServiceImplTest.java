package com.charliemulic.target.myretail.services;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.converters.ProductCommandToProduct;
import com.charliemulic.target.myretail.converters.ProductPriceToProductPriceCommand;
import com.charliemulic.target.myretail.converters.ProductToProductCommand;
import com.charliemulic.target.myretail.errors.EntityNotFoundException;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.model.ProductPrice;
import com.charliemulic.target.myretail.model.tcin.*;
import com.charliemulic.target.myretail.repositories.ProductsRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    private static final String PRODUCT_ID = "1";
    private static final String PRODUCT_NAME = "Test Product";
    private static final String PRODUCT_CURRENCY_CODE = "USD";
    private static final Double PRODUCT_PRICE = 1.23;
    public static final String ENDPOINT_URL = "https://redsky.target.com/v2/pdp/tcin/1?excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    @Mock
    ProductsRepository repository;

    @Mock
    ProductCommandToProduct productCommandToProduct;

    @Mock
    ProductToProductCommand productToProductCommand;

    @Mock
    RestTemplate restTemplate;

    ProductService service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ProductServiceImpl(repository, productCommandToProduct, productToProductCommand, restTemplate);
    }

    @Test
    public void getProductByIdTest() {
        // when
        Product testProduct = createTestProduct();
        when(repository.findById("1")).thenReturn(Optional.of(testProduct));
        Product product = service.getProductById("1");

        // then
        assertNotNull(product);
        assertEquals("1", product.getId());
        assertEquals("Test Product", product.getName());
        assertNotNull(product.getCurrentPrice());
    }

    @Test
    public void getProductByIdThrowsExceptionForNonExistantId() {
        thrown.expect(EntityNotFoundException.class);

        // when
        when(repository.findById("1")).thenReturn(Optional.ofNullable(null));
        Product product = service.getProductById("1");
    }

    @Test
    public void saveProductCommandTest() {
        ProductCommand productCommand = createTestProductCommand();
        Product product = createTestProduct();

        when(repository.save(product)).thenReturn(product);
        when(productCommandToProduct.convert(productCommand)).thenReturn(product);
        when(productToProductCommand.convert(product)).thenReturn(productCommand);

        ProductCommand savedProductCommand = service.saveProductCommand(productCommand);

        assertNotNull(savedProductCommand);
        assertEquals(PRODUCT_ID, savedProductCommand.getId());
    }

    @Test
    public void getProductNameTest() {
        Tcin testTcin = createTestTcin();
//        when(restTemplate.getForObject(any(), any())).thenReturn(testTcin);
        when(restTemplate.getForObject(ENDPOINT_URL, Tcin.class)).thenReturn(testTcin);

        String name = service.getProductName("1").join();
        assertEquals("Test Name", name);
    }

    @Test
    public void copyProductDetailsForIdTest() {
        ProductCommand productCommand = createTestProductCommand();
        Product product = createTestProduct();
        Tcin testTcin = createTestTcin();

        when(restTemplate.getForObject(ENDPOINT_URL, Tcin.class)).thenReturn(testTcin);
        when(repository.save(product)).thenReturn(product);
        when(productCommandToProduct.convert(any())).thenReturn(product);
        when(productToProductCommand.convert(any())).thenReturn(productCommand);

        Boolean result = service.copyProductDetailsForId("1").join();
        assertEquals(true, result);
    }

    @Test
    public void copyProductDetailsForIdFailedTest() {
        when(restTemplate.getForObject(ENDPOINT_URL, Tcin.class))
                .thenThrow(HttpClientErrorException.class);

        Boolean result = service.copyProductDetailsForId("1").join();
        assertEquals(false, result);
    }

    @Test
    public void getPriceFromDbTest() {
        Product testProduct = createTestProduct();
        when(repository.findById("1")).thenReturn(Optional.of(testProduct));
        Double price = service.getPriceFromDb("1").join();

        assertEquals(PRODUCT_PRICE, price);
    }

    @Test
    public void getAllProductsTest() {
        // when
        Product testProduct = createTestProduct();
        when(repository.findAll()).thenReturn(Lists.list(testProduct));
        List<Product> products = service.getAllProducts();

        // then
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getId());
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

    private Tcin createTestTcin() {
        Tcin tcin = new Tcin();
        com.charliemulic.target.myretail.model.tcin.Product product = new com.charliemulic.target.myretail.model.tcin.Product();
        Item item = new Item();
        item.setTcin("1");
        ProductDescription description = new ProductDescription();
        description.setTitle("Test Name");
        item.setProductDescription(description);
        product.setItem(item);
        Price price = new Price();
        OfferPrice offerPrice = new OfferPrice();
        offerPrice.setPrice(5.43);
        price.setOfferPrice(offerPrice);
        product.setPrice(price);
        tcin.setProduct(product);
        return tcin;
    }

    private ProductCommand createTestProductCommand() {
        return new ProductToProductCommand(new ProductPriceToProductPriceCommand()).convert(createTestProduct());
    }
}