package com.charliemulic.target.myretail.controllers;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.errors.EntityNotFoundException;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

// TODO tests
// TODO command object validation
// TODO logging
// TODO generate items in my database from target's api?

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get a product with a given id. Satisfies the requirements:
     * - Responds to an HTTP GET request at /products/{id} and delivers product data as JSON (where {id} will be a number.
     * - Example product IDs: 15117729, 16483589, 16696652, 16752456, 15643793)
     * - Example response: {"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}
     * @param id - the id of the product to be retrieved
     * @return - (200) a JSON representation of the product with the provided id, (404) no product with given id
     */
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new EntityNotFoundException();
        }
        return product;
    }

    /**
     * Attempts to update the product with the given id with the JSON in the post body.
     * Satisfies requirements:
     * - BONUS: Accepts an HTTP PUT request at the same path (/products/{id}),
     *   containing a JSON request body similar to the GET response, and updates the product’s price in the data store.
     */
    @PutMapping("/products/{id}")
    public String updateProductById(@ModelAttribute ProductCommand product) {
        ProductCommand savedProduct = productService.saveProductCommand(product);
        return "redirect:/products/" + savedProduct.getId();
    }

    /**
     * Performs an HTTP GET to retrieve the product name from an external API. (For this exercise the data will come from redsky.target.com, but let’s just pretend this is an internal resource hosted by myRetail)  
     * Example: http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics
     * TODO Reads pricing information from a NoSQL data store and combines it with the product id and name from the HTTP request into a single response.  
     */
    @GetMapping("/products/{id}/name")
    public Map<String, Object> getProductName(@PathVariable Long id) throws Exception {
        CompletableFuture<String> nameFuture = productService.getProductName(id);
        CompletableFuture<Double> priceFuture = productService.getPriceFromDb(id);
        CompletableFuture.allOf(nameFuture, priceFuture).join(); // wait for all of our results

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", nameFuture.get());
        result.put("price", priceFuture.get());
        return result;
    }

    public void copyProductDataToLocalDb(@PathVariable Long id) {

    }


}
