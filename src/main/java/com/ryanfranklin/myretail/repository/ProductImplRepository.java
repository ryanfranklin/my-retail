package com.ryanfranklin.myretail.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanfranklin.myretail.exception.NotFoundException;
import com.ryanfranklin.myretail.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Repository
public class ProductImplRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** The resource URL to get the product name by id **/
    private static final String PRODUCT_NAME_RESOURCE_URL = "http://redsky.target.com/v2/pdp/tcin/{id}?" +
            "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics," +
            "question_answer_statistics,deep_red_labels,available_to_promise_network";

    /** The URL variable tag for the id of the product **/
    private static final String PATH_VARIABLE_TAG_ID = "id";

    /** The json path for the product name is: product.item.product_description.title **/
    private static final String JSON_TAG_PRODUCT = "product";
    private static final String JSON_TAG_ITEM = "item";
    private static final String JSON_TAG_PRODUCT_DESCRIPTION = "product_description";
    private static final String JSON_TAG_TITLE = "title";

    @Autowired
    private ProductCurrentPriceRepository currentPriceRepository;

    public List<Product> findAll() {

        List<Product.CurrentPrice> currentPrices = currentPriceRepository.findAll();
        List<Product> products = new ArrayList<>();

        for (Product.CurrentPrice currentPrice : currentPrices) {
            String name = getName(currentPrice.getId());
            products.add(new Product(currentPrice.getId(), name, currentPrice));
        }

        return products;
    }

    /**
     * Gets a {@link Product} by {@code id}.
     * @param id the id of the product, not null
     * @return a product, not null
     */
    public Product findOne(String id) {

        checkNotNull(id);

        String name = getName(id);
        Product.CurrentPrice currentPrice = currentPriceRepository.findOne(id);

        if (currentPrice == null) {
            logger.debug("Could not find the product's current price by id: {}", id);
            throw new NotFoundException();
        }
        return new Product(id, name, currentPrice);
    }

    /**
     * Gets a {@link Product} name by {@code id}.
     * @param id the id of the product, not null
     * @return a product name, not null
     */
    private String getName(String id) {

        checkNotNull(id);

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(PATH_VARIABLE_TAG_ID, id);

        ResponseEntity<String> response;
        try {
            response =  restTemplate.getForEntity(PRODUCT_NAME_RESOURCE_URL, String.class, uriVariables);
        } catch (HttpClientErrorException e)   {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            } else {
                logger.debug("Could not find the product's name by id: {}", id);
                throw new NotFoundException();
            }
        }

        String responseBody = response.getBody();
        logger.trace("Response body to get product name by id: {}: {}", id, responseBody);

        JsonNode root;
        try {
            root = new ObjectMapper().readTree(responseBody);
        } catch (IOException e) {
            logger.debug("Unable to parse the response body to get the product name.", e);
            throw new RestClientException("Unable to read product name.");
        }

        JsonNode nameNode = root.path(JSON_TAG_PRODUCT).path(JSON_TAG_ITEM).path(JSON_TAG_PRODUCT_DESCRIPTION).path(JSON_TAG_TITLE);
        String name = nameNode.asText();

        if (name == null || name.isEmpty()) {
            logger.debug("Unable to read product name from response body: {}.", responseBody);
            throw new RestClientException("Unable to read product name.");
        } else {
            return name;
        }
    }

    /**
     * Updates a {@link Product} by {@code id}.
     *
     * @param id the id of the product, not null
     * @param product the product details to update
     */
    public void update(String id, @Valid Product product) {

        checkNotNull(id);

        String name = product.getName();
        // Update product name here when implementation becomes known

        Product.CurrentPrice currentPrice = currentPriceRepository.findOne(id);

        if (currentPrice == null) {
            logger.debug("Could not find the product's current price by id: {}", id);
            throw new NotFoundException();
        }

        Product.CurrentPrice newPrice = product.getCurrentPrice();
        currentPrice.setValue(newPrice.getValue());
        currentPrice.setCurrencyCode(newPrice.getCurrencyCode());

        currentPriceRepository.save(currentPrice);
    }
}
