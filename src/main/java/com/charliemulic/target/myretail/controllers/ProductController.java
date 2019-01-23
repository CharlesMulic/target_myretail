package com.charliemulic.target.myretail.controllers;

import com.charliemulic.target.myretail.commands.ProductCommand;
import com.charliemulic.target.myretail.errors.EntityNotFoundException;
import com.charliemulic.target.myretail.errors.ValidationErrorsException;
import com.charliemulic.target.myretail.model.Product;
import com.charliemulic.target.myretail.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/products/", "/products"})
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Get a product with a given id.
     * Satisfies the requirements:
     * - Responds to an HTTP GET request at /products/{id} and delivers product data as JSON (where {id} will be a number.
     * - Example product IDs: 15117729, 16483589, 16696652, 16752456, 15643793)
     * - Example response: {"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}
     * @param id - the id of the product to be retrieved
     * @return - (200) a JSON representation of the product with the provided id, (404) no product with given id
     */
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable String id) {
        log.info(String.format("Retrieving product with id: %s", id));
        return productService.getProductById(id);
    }

    /**
     * Attempts to update the product with the given id with the JSON in the post body.
     * Satisfies requirements:
     * - BONUS: Accepts an HTTP PUT request at the same path (/products/{id}),
     *   containing a JSON request body similar to the GET response, and updates the product’s price in the data store.
     * @param id - The id of the product to update
     * @param product - The command object that the request body data will be bound to
     * @return - a redirection to GET /products/{id}
     */
    @PutMapping("/products/{id}")
    public String updateProductById(@PathVariable String id, @Valid @RequestBody ProductCommand product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationErrorsException(bindingResult);
        }
        log.info(String.format("Updating product with id: %s", id));
        product.setId(id);
        ProductCommand savedProduct = productService.saveProductCommand(product);
        return "redirect:/products/" + savedProduct.getId();
    }

    /**
     * Fetches the product name and price from different sources and aggregates the results.
     * Satisfies requirements:
     * - Performs an HTTP GET to retrieve the product name from an external API. (For this exercise the data will come from redsky.target.com, but let’s just pretend this is an internal resource hosted by myRetail)  
     * - Example: http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics
     * - Reads pricing information from a NoSQL data store and combines it with the product id and name from the HTTP request into a single response.  
     */
    @GetMapping("/products/{id}/name")
    public Map<String, Object> getProductNameAndPrice(@PathVariable String id) throws Exception {
        log.info(String.format("Fetching product and pricing details for product id: %s", id));
        CompletableFuture<String> nameFuture = productService.getProductName(id);
        CompletableFuture<Double> priceFuture = productService.getPriceFromDb(id);
        CompletableFuture.allOf(nameFuture, priceFuture).join(); // proceed when we have all of our results
        log.info("Finished aggregating asynchronous tasks");

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", nameFuture.get());
        result.put("price", priceFuture.get());

        return result;
    }

    /**
     * Utility endpoint used to populate additional data into the Mongo database. If the id is a valid product when
     * hitting the third party API, a record for that product will be created in this applications Mongo database.
     * @param id - The id of the product to copy
     */
    @GetMapping("/products/{id}/copy")
    public String copyProductDataToLocalDb(@PathVariable String id) {
        if (!productService.copyProductDetailsForId(id).join()) {
            throw new EntityNotFoundException(String.format("Unable to retrieve data for id: %s", id));
        }
        return "Data Copied Successfully";
    }


}
