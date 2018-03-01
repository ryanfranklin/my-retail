package com.ryanfranklin.myretail.controller;

import com.ryanfranklin.myretail.model.Product;
import com.ryanfranklin.myretail.repository.ProductImplRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductImplRepository productImplRepository;


    @RequestMapping(method=RequestMethod.GET)
    public Iterable<Product> getProducts() {
        return productImplRepository.findAll();
    }

    @RequestMapping(method= RequestMethod.GET, value="/{id}")

    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productImplRepository.findOne(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.PUT, value="/{id}", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProductById(@PathVariable String id, @RequestBody @Valid Product product) {

        productImplRepository.update(id, product);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
