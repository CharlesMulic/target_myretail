package com.charliemulic.target.myretail.controllers;

import com.charliemulic.target.myretail.MyretailApplication;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyretailApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers;

    @Before
    public void setUp() {
        headers = new HttpHeaders();
    }

    @Test
    public void getProducts_shouldReturnListOfProductsAsJson() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/products"),
                HttpMethod.GET, entity, String.class);

        String expected = "[{\"id\":\"13860420\",\"name\":\"Final Destination 5 (dvd_video)\",\"currentPrice\":{\"value\":7.29,\"currencyCode\":\"USD\"}},{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"currentPrice\":{\"value\":14.99,\"currencyCode\":\"USD\"}},{\"id\":\"13860421\",\"name\":\"Revolutionary girl utena:Apoc box 3 (DVD)\",\"currentPrice\":{\"value\":51.99,\"currencyCode\":\"USD\"}},{\"id\":\"13860425\",\"name\":\"Godzilla (Blu-ray)\",\"currentPrice\":{\"value\":26.19,\"currencyCode\":\"USD\"}},{\"id\":\"13860424\",\"name\":\"Moment of truth (Blu-ray)\",\"currentPrice\":{\"value\":19.69,\"currencyCode\":\"USD\"}},{\"id\":\"13860429\",\"name\":\"SpongeBob SquarePants: SpongeBob's Frozen Face-off\",\"currentPrice\":{\"value\":7.5,\"currencyCode\":\"USD\"}}]";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void getProductById_shouldReturnProductAsJson() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/products/13860420"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"id\":\"13860420\",\"name\":\"Final Destination 5 (dvd_video)\",\"currentPrice\":{\"value\":7.29,\"currencyCode\":\"USD\"}}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Ignore // breaks depending on test run order
    public void putProductById_shouldRedirectonSuccess() throws Exception {
        String body = "{\"id\":1,\"name\":\"Test Product Change\",\"currentPrice\":{\"currencyCode\":\"USD\",\"value\":1.23}}";
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/products/1"),
                HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
    }

    @Test
    public void putProductById_shouldRejectValidationErrors() throws Exception {
        String body = "{\"id\":1,\"name\":\"Test Product Change\",\"currentPrice\":{}}";
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/products/1"),
                HttpMethod.PUT, entity, String.class);

        String expected = "{\"validationErrors\":[\"Field [currentPrice.currencyCode] must not be null\",\"Field [currentPrice.value] must not be null\"],\"status\":\"BAD_REQUEST\",\"statusCode\":400}";

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void getProductName_shouldReturnNameAndPriceJson() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/products/13860420/name"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"price\":7.29,\"name\":\"Final Destination 5 (dvd_video)\",\"id\":\"13860420\"}";

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void copy_shouldReturnSuccessMessage() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/products/13860420/copy"),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data Copied Successfully", response.getBody());
    }

    @Test
    public void copy_shouldReturnErrorMessage() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/products/1/copy"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"status\":\"NOT_FOUND\",\"statusCode\":404,\"timestamp\":\"23-01-2019 04:02:12\",\"message\":\"Unable to retrieve data for id: 1\"}";

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(),
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("timestamp", (o1, o2) -> true)));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}