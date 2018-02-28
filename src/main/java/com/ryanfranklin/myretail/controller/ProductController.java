package com.ryanfranklin.myretail.controller;

import com.ryanfranklin.myretail.model.Product;
import com.ryanfranklin.myretail.repository.ProductCurrentPriceRepository;
import com.ryanfranklin.myretail.repository.ProductImplRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
class ProductController {

    @Autowired
    private ProductCurrentPriceRepository currentPriceRepository;
    @Autowired
    private ProductImplRepository productImplRepository;


    @RequestMapping(method=RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public void createProduct(@RequestBody @Valid Product product) {
        Product.CurrentPrice currentPrice = product.getCurrentPrice();
        currentPriceRepository.save(currentPrice);
    }

    @RequestMapping(method=RequestMethod.GET)
    public Iterable<Product> getProduct() {
        return productImplRepository.findAll();
    }

    @RequestMapping(method= RequestMethod.GET, value="/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productImplRepository.findOne(id);
        if (product==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/{id}", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProductById(@PathVariable String id, @RequestBody @Valid Product product) {

        String name = product.getName();
        // Update name here when API is known

        Product.CurrentPrice newPrice = product.getCurrentPrice();
        Product.CurrentPrice currentPrice = currentPriceRepository.findOne(id);
        if (currentPrice==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPrice.setValue(newPrice.getValue());
        currentPrice.setCurrencyCode(newPrice.getCurrencyCode());

        currentPriceRepository.save(currentPrice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method= RequestMethod.DELETE, value="/{id}")
    public void deleteProductById(@PathVariable String id) {
        currentPriceRepository.delete(id);
    }

}
