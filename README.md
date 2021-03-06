[![CircleCI](https://circleci.com/gh/CharlesMulic/target_myretail.svg?style=svg)](https://circleci.com/gh/CharlesMulic/target_myretail)

CircleCI build: https://circleci.com/gh/CharlesMulic/target_myretail

This is a Spring Boot MVC application exposing several RESTful endpoints described below. The application is currently configured to run with an embedded MongoDB database, but can be easily changed to a remote host.

You can run the application from command line with: mvn spring-boot:run
Alternatively, run via an IDE by creating a run configuration for the main class com.charliemulic.target.myretail.MyretailApplication

<h3>Endpoints</h3>

- GET /api/v1/products

<p>Returns a list of all products in the database</p>

```json
Example Request: GET http://localhost:8080/api/v1/products
[
  {
    "id": "13860424",
    "name": "Moment of truth (Blu-ray)",
    "currentPrice": {
      "value": 19.69,
      "currencyCode": "USD"
    }
  },
  {
    "id": "13860429",
    "name": "SpongeBob SquarePants: SpongeBob's Frozen Face-off",
    "currentPrice": {
      "value": 7.5,
      "currencyCode": "USD"
    }
  }
]
```

- GET /api/v1/products/{id}

<p>Provides information for a specific product with the provided id</p>

```json
Example Request: GET http://localhost:8080/api/v1/products/13860428/
{
  "id":13860428,
  "name":"The Big Lebowski (Blu-ray) (Widescreen)",
  "current_price": {
    "value": 13.49,
    "currency_code":"USD"
  }
}
```

- PUT /api/v1/products/{id}

<p>Attempts to update the product with the provided id with the JSON data in the request body. If valid, you should be redirected to GET products/{id}, if invalid, you should receive a response describing the problem.</p>

```json
Example Request: PUT http://localhost:8080/api/v1/products/13860428/
Body:
{
  "id":13860428,
  "name":"Edited Name",
  "current_price": {
    "value": 13.49,
    "currency_code":"USD"
  }
}
```

- GET /api/v1/products/{id}/name

This endpoint uses a rest client to hit a third party API that will provide the name of the product with this id. Additionally, it will asynchronously fetch pricing information from a local database, and return the aggregated results as JSON.

```json
Example Request: GET http://localhost:8080/api/v1/products/13860428/name
{
"price": 10.5,
"name": "The Big Lebowski (Blu-ray)",
"id": 13860428
}
```

- GET /api/v1/products/{id}/copy

This endpoint fetches the product with the given id from a third party and copies it to the local application database. This is a utility endpoint intended to populate demo data.

```json
Example Request: GET http://localhost:8080/api/v1/products/13860424/copy
Example Response: Data Copied Successfully
```